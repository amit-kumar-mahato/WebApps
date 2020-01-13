package com.blbz.fundoonotes.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blbz.fundoonotes.customexception.LabelAlreadyExistException;
import com.blbz.fundoonotes.dto.LabelDto;
import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.Note;
import com.blbz.fundoonotes.model.User;
import com.blbz.fundoonotes.repository.LabelRepository;
import com.blbz.fundoonotes.repository.NoteRepository;
import com.blbz.fundoonotes.repository.UserRepository;
import com.blbz.fundoonotes.service.LabelService;
import com.blbz.fundoonotes.utility.JwtGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LabelServiceImpl implements LabelService {

	@Autowired
	JwtGenerator jwtGenerator;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	LabelRepository labelRepository;

	@Autowired
	NoteRepository noterepository;

	@Autowired
	Label label;

	@Override
	public boolean createlabel(LabelDto labelDto, String token) {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			String labelName = labelDto.getName();
			LabelDto labelInfo = labelRepository.findOneByName(labelName);
			if (labelInfo == null) {
				label = modelMapper.map(labelDto, Label.class);
				label.setUserLabel(isUserAvailable.get());
				labelRepository.save(label);
				return true;
			} else {
				//throw new LabelAlreadyExistException("Label is already exist...");
			}
		}
		return false;
	}

	@Override
	public boolean createOrMapWithNote(LabelDto labelDto, long noteId, String token) throws LabelAlreadyExistException {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			String labelName = labelDto.getName();
			LabelDto labelInfo = labelRepository.findOneByName(labelName);
			if (labelInfo == null) {
				Label label = modelMapper.map(labelDto, Label.class);
				label.setUserLabel(isUserAvailable.get());
				labelRepository.save(label);

				Optional<Note> noteInfo = noterepository.findById(noteId);
				if (noteInfo.isPresent()) {
					noteInfo.get().getLabels().add(label);
					noterepository.save(noteInfo.get());
					return true;
				}
			} else {
				throw new LabelAlreadyExistException("Label is already exist...");
			}
		}
		return false;
	}

	@Override
	public boolean removeLabels(String token, long noteId, long labelId) {
		Optional<Note> isNoteAvailable = noterepository.findById(noteId);
		if (isNoteAvailable.isPresent()) {
			Optional<Label> isLabelAvailable = labelRepository.findById(labelId);
			if (isLabelAvailable.isPresent()) {
				isNoteAvailable.get().getLabels().remove(isLabelAvailable.get());
				noterepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deletepermanently(String token, long labelId) {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Label> isLabelAvailable = labelRepository.findById(labelId);
			if (isLabelAvailable.isPresent()) {
				isLabelAvailable.get().setNoteList(null);
				/*
				 * List<Note> noteDetails = isLabelAvailable.get().getNoteList(); for(Note n:
				 * noteDetails) { n.getNoteId(); }
				 */
				labelRepository.deleteMapping(labelId);
				//labelRepository.save(isLabelAvailable.get());
				labelRepository.deleteById(labelId);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateLabel(String token, long labelId, LabelDto labelDto) {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Label> isLabelAvailable = labelRepository.findById(labelId);
			if (isLabelAvailable.isPresent()) {
				isLabelAvailable.get().setName(labelDto.getName());
				labelRepository.save(isLabelAvailable.get());
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Label> getAllLabels(String token) {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			return (List<Label>) labelRepository.findAll();
		}
		return null;
	}

	@Override
	public List<Note> getAllNotes(String token, long labelId) {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Label> isLabelAvailable = labelRepository.findById(labelId);
			if (isLabelAvailable.isPresent()) {
				List<Note> list = isLabelAvailable.get().getNoteList();
				log.info("Note List :"+list);
				return list;
			}
		}
		return null;
	}

	@Override
	public boolean addLabels(String token, long noteId, long labelId) {

		Optional<Note> isNoteAvailable = noterepository.findById(noteId);
		if (isNoteAvailable.isPresent()) {
			Optional<Label> isLabelAvailable = labelRepository.findById(labelId);
			if (isLabelAvailable.isPresent()) {
				log.info("Note :" + isNoteAvailable.get().getTitle());
				/*
				 * isLabelAvailable.get().getNoteList().add(isNoteAvailable.get());
				 * labelRepository.save(isLabelAvailable.get());
				 */
				isNoteAvailable.get().getLabels().add(isLabelAvailable.get());
				noterepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}
}
