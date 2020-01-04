package com.blbz.fundoonotes.serviceimpl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blbz.fundoonotes.customexception.LabelAlreadyExistException;
import com.blbz.fundoonotes.dto.LabelDto;
import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.User;
import com.blbz.fundoonotes.repository.LabelRepository;
import com.blbz.fundoonotes.repository.UserRepository;
import com.blbz.fundoonotes.service.LabelService;
import com.blbz.fundoonotes.utility.JwtGenerator;

@Service
public class LabelServiceImpl implements LabelService {
	
	@Autowired
	JwtGenerator jwtGenerator;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	LabelRepository labelRepository;

	@Override
	public boolean createlabel(LabelDto labelDto, String token) throws Exception {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable.isPresent()) {
			String labelName = labelDto.getName();
			LabelDto label = labelRepository.findOneByName(labelName);
			if(label!=null) {
				Label labelInfo = modelMapper.map(labelDto, Label.class);
				labelInfo.setUserLabel(isUserAvailable.get());
				labelRepository.save(labelInfo);
				return true;	
			}else {
				throw new LabelAlreadyExistException("Label is already exist...");
			}
		}
		return false;
	}

	@Override
	public boolean createOrMapWithNote(LabelDto labelDto, long noteId, String token) throws Exception{
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable.isPresent()) {
			
		}
		return false;
	}
	
}
