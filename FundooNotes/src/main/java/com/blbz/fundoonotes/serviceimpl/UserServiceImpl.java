package com.blbz.fundoonotes.serviceimpl;

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
import com.blbz.fundoonotes.customexception.EmailNotFoundException;
import com.blbz.fundoonotes.customexception.UserNotVerifiedException;
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
	public User registration(UserDto userDto) {
		//User userDetails = new User();

		Optional<User> checkEmailAvailability = userRepository.findOneByEmail(userDto.getEmail());
		if(checkEmailAvailability.isPresent()) {
			throw new EmailAlreadyExistException(userDto.getEmail()+" Already Exist");
		}
		else{
			User userDetails = modelMapper.map(userDto, User.class);
			userDetails.setCreatedAt(Utility.dateTime());
			String password = encryption.encode(userDto.getPswd());
			userDetails.setPswd(password);

			userRepository.save(userDetails);

			String response = mailresponse.formMessage("http://localhost:8081/users/verify/",
					generate.jwtToken(userDetails.getUserId()));
			log.info("Response URL :" + response);
			mailObject.setEmail(userDto.getEmail());
			mailObject.setMessage(response);
			mailObject.setSubject("verification");

			rabbitMQSender.send(mailObject);
			return userDetails;
		} 

	}

	@Override
	public List<User> getAllDetails() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public boolean updateDetails(User user) {
		long userId = user.getUserId();
		String pswd = user.getPswd();

		String encrpswd = Utility.passwordEncoder(pswd);

		Optional<User> userinfo = userRepository.findOneByUserIdAndPswd(userId, encrpswd);
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

		if (userInfo.isPresent()) {
			log.info("User Information " + userInfo);
			if ((userInfo.get().isVerified())
					&& encryption.matches(loginDetails.getPassword(), userInfo.get().getPswd())) {

				log.info("Generated Token :" + generate.jwtToken(userInfo.get().getUserId()));

				return userInfo.get();
			} else {
				String response = mailresponse.formMessage("http://localhost:8081/users/verify",
						generate.jwtToken(userInfo.get().getUserId()));

				log.info("Verification Link :" + response);

				MailServiceProvider.sendEmail(loginDetails.getEmail(), "verification", response);

				throw new UserNotVerifiedException("Invalid credentials");
			}

		} else {
			throw new EmailNotFoundException(loginDetails.getEmail()+"Please register before login");
		}
	}

	@Transactional
	@Override
	public boolean verify(String token) {
		log.info("id in verification" + (long) generate.parseJWT(token));
		Long id = (long) generate.parseJWT(token);
		Optional<User> userInfo = userRepository.findById(id);
		if (userInfo.isPresent()) {
			userInfo.get().setVerified(true);
			userRepository.save(userInfo.get());
			return true;
		}
		return false;
	}

	@Override
	public boolean isUserAvailable(String email) {
		Optional<User> isUserAvailable = userRepository.findOneByEmail(email);
		if (isUserAvailable.isPresent() && isUserAvailable.get().isVerified() == true) {
			String response = mailresponse.formMessage("http://localhost:3000/updatePassword",
					generate.jwtToken(isUserAvailable.get().getUserId()));
			MailServiceProvider.sendEmail(isUserAvailable.get().getEmail(), "Update Password", response);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public User updatePassword(String token, Updatepassword pswd) {
		if (pswd.getNewPassword().equals(pswd.getCnfPassword())) {
			log.info("Getting id from token :" + generate.parseJWT(token));
			long id = generate.parseJWT(token);
			Optional<User> isUserIdAvailable = userRepository.findById(id);
			if (isUserIdAvailable.isPresent()) {
				String password = encryption.encode(pswd.getNewPassword());
				isUserIdAvailable.get().setPswd(password);
				userRepository.save(isUserIdAvailable.get());
				return isUserIdAvailable.get();
			}
		}
		return null;
	}

	@Override
	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}

}
