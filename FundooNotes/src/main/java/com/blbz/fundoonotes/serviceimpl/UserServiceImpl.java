package com.blbz.fundoonotes.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blbz.fundoonotes.configuration.RabbitMQSender;
import com.blbz.fundoonotes.dto.LoginDetails;
import com.blbz.fundoonotes.dto.Updatepassword;
import com.blbz.fundoonotes.dto.UserDto;
import com.blbz.fundoonotes.model.User;
import com.blbz.fundoonotes.repository.UserRepository;
import com.blbz.fundoonotes.responses.MailObject;
import com.blbz.fundoonotes.responses.MailResponse;
import com.blbz.fundoonotes.service.IUserService;
import com.blbz.fundoonotes.utility.JwtGenerator;
import com.blbz.fundoonotes.utility.MailServiceProvider;
import com.blbz.fundoonotes.utility.Utility;

@Service
public class UserServiceImpl implements IUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MailResponse mailresponse;

	@Autowired
	private JwtGenerator generate;

	@Autowired
	private MailObject mailObject;

	@Autowired
	private RabbitMQSender rabbitMQSender;
	
	@Autowired
	private BCryptPasswordEncoder encryption;

	@Transactional
	@Override
	public boolean registration(UserDto userDto) {
		User userDetails = new User();

		User checkEmailAvailability = userRepository.findOneByEmail(userDto.getEmail());
		if (checkEmailAvailability == null) {
			userDetails = modelMapper.map(userDto, User.class);
			userDetails.setCreatedAt(Utility.dateTime());
			//userDetails.setPassword(Utility.passwordEncoder(userDto.getPassword()));
			String password = encryption.encode(userDto.getPassword());
			userDetails.setPassword(password);

			userRepository.save(userDetails);

			String response = mailresponse.formMessage("http://localhost:8081/users/verify/",
					generate.jwtToken(userDetails.getUserId()));
			LOGGER.info("Response URL :" + response);

			mailObject.setEmail(userDto.getEmail());
			mailObject.setMessage(response);
			mailObject.setSubject("verification");

			rabbitMQSender.send(mailObject);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<User> getAllDetails() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public Map<String, Object> findByIdUserId(long userId) {
		Optional<User> isUserIdAvailable = userRepository.findById(userId);
		Map<String, Object> map = null;
		if (isUserIdAvailable.isPresent()) {
			map = new HashMap<String, Object>();
			map.put("UserId", isUserIdAvailable.get().getUserId());
			map.put("FirstName", isUserIdAvailable.get().getFirstName());
			map.put("LastName", isUserIdAvailable.get().getLastName());
			map.put("UserName", isUserIdAvailable.get().getUserName());
			map.put("ContactNumber", isUserIdAvailable.get().getMobile());
		} else {

		}
		return map;
	}

	@Override
	public boolean updateDetails(User user) {
		long userId = user.getUserId();
		String pswd = user.getPassword();

		String encrpswd = Utility.passwordEncoder(pswd);

		Optional<User> userinfo = userRepository.findOneByUserIdAndPassword(userId, encrpswd);
		if (userinfo.isPresent()) {
			userRepository.save(user);
			return true;
		}
		return false;
	}

	@Override
	public boolean findByUserId(long userId) {

		Optional<User> isUserIdAvailable = userRepository.findById(userId);
		if (isUserIdAvailable.isPresent()) {
			userRepository.deleteById(userId);
			return true;
		}
		return false;
	}

	@Transactional
	@Override
	public User login(LoginDetails loginDetails) {
		User userInfo = userRepository.findOneByEmail(loginDetails.getEmail());
		LOGGER.info("User Information " + userInfo);

		if (userInfo != null) {
			/*
			 * if ((userInfo.getIsVerified()) &&
			 * Utility.matches(Utility.passwordEncoder(loginDetails.getPassword()),
			 * userInfo.getPassword())) {
			 */
			if ((userInfo.getIsVerified())
					&& encryption.matches(loginDetails.getPassword(), userInfo.getPassword())) {

				LOGGER.info("Generated Token :" + generate.jwtToken(userInfo.getUserId()));

				return userInfo;
			} else {
				String response = mailresponse.formMessage("http://localhost:8081/users/verify",
						generate.jwtToken(userInfo.getUserId()));

				LOGGER.info("Verification Link :" + response);

				MailServiceProvider.sendEmail(loginDetails.getEmail(), "verification", response);

				return null;
			}

		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public boolean verify(String token) throws Exception {
		LOGGER.info("id in verification" + (long) generate.parseJWT(token));
		Long id = (long) generate.parseJWT(token);
		Optional<User> userInfo = userRepository.findById(id);
		if (userInfo.isPresent()) {
			userInfo.get().setIsVerified(true);
			userRepository.save(userInfo.get());
			return true;
		}
		// userRepository.verify(id);
		return false;
	}

	@Override
	public boolean isUserAvailable(String email) {
		User isUserAvailable = userRepository.findOneByEmail(email);
		if (isUserAvailable != null && isUserAvailable.getIsVerified() == true) {
			String response = mailresponse.formMessage("http://localhost:8081/users/passwordupdate",
					generate.jwtToken(isUserAvailable.getUserId()));
			MailServiceProvider.sendEmail(isUserAvailable.getEmail(), "Update Password", response);
			
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean updatePassword(String token, Updatepassword pswd) throws Exception {
		if(pswd.getNewPassword().equals(pswd.getCnfPassword())) {
			LOGGER.info("Getting id from token :"+generate.parseJWT(token));
			long id = generate.parseJWT(token);
			Optional<User> isIdAvailable = userRepository.findById(id);
			if(isIdAvailable.isPresent()) {
				isIdAvailable.get().setPassword(Utility.passwordEncoder(pswd.getNewPassword()));
				userRepository.save(isIdAvailable.get());
				return true;
			}
		}
		return false;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = (List<User>) userRepository.findAll();
		return users;
	}
}
