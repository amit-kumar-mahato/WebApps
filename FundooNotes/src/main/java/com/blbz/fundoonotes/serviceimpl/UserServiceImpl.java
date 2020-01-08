package com.blbz.fundoonotes.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blbz.fundoonotes.configuration.RabbitMQSender;
import com.blbz.fundoonotes.customexception.EmailAlreadyExistException;
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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

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

		Optional<User> checkEmailAvailability = userRepository.findOneByEmail(userDto.getEmail());
		if (!checkEmailAvailability.isPresent()) {
			userDetails = modelMapper.map(userDto, User.class);
			userDetails.setCreatedAt(Utility.dateTime());
			// userDetails.setPassword(Utility.passwordEncoder(userDto.getPassword()));
			String password = encryption.encode(userDto.getPassword());
			userDetails.setPassword(password);

			userRepository.save(userDetails);

			String response = mailresponse.formMessage("http://localhost:8081/users/verify/",
					generate.jwtToken(userDetails.getUserId()));
			log.info("Response URL :" + response);
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
		Optional<User> userInfo = userRepository.findOneByEmail(loginDetails.getEmail());
		log.info("User Information " + userInfo);

		if (userInfo.isPresent()) {
			/*
			 * if ((userInfo.getIsVerified()) &&
			 * Utility.matches(Utility.passwordEncoder(loginDetails.getPassword()),
			 * userInfo.getPassword())) {
			 */
			if ((userInfo.get().getIsVerified())
					&& encryption.matches(loginDetails.getPassword(), userInfo.get().getPassword())) {

				log.info("Generated Token :" + generate.jwtToken(userInfo.get().getUserId()));

				return userInfo.get();
			} else {
				String response = mailresponse.formMessage("http://localhost:8081/users/verify",
						generate.jwtToken(userInfo.get().getUserId()));

				log.info("Verification Link :" + response);

				MailServiceProvider.sendEmail(loginDetails.getEmail(), "verification", response);

				return null;
			}

		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public boolean verify(String token) {
		log.info("id in verification" + (long) generate.parseJWT(token));
		Long id = (long) generate.parseJWT(token);
		Optional<User> userInfo = userRepository.findById(id);
		if (userInfo.isPresent()) {
			userInfo.get().setIsVerified(true);
			userRepository.save(userInfo.get());
			return true;
		}
		return false;
	}

	@Override
	public boolean isUserAvailable(String email) {
		Optional<User> isUserAvailable = userRepository.findOneByEmail(email);
		if (isUserAvailable.isPresent() && isUserAvailable.get().getIsVerified() == true) {
			String response = mailresponse.formMessage("http://localhost:8081/users/passwordupdate",
					generate.jwtToken(isUserAvailable.get().getUserId()));
			MailServiceProvider.sendEmail(isUserAvailable.get().getEmail(), "Update Password", response);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updatePassword(String token, Updatepassword pswd) {
		if (pswd.getNewPassword().equals(pswd.getCnfPassword())) {
			log.info("Getting id from token :" + generate.parseJWT(token));
			long id = generate.parseJWT(token);
			Optional<User> isIdAvailable = userRepository.findById(id);
			if (isIdAvailable.isPresent()) {
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
