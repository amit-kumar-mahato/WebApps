package com.blbz.fundoonotes.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blbz.fundoonotes.customexception.LabelAlreadyExistException;
import com.blbz.fundoonotes.customexception.UserNotFoundException;
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
	public Label createlabel(String labelName, String token) {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			// String labelName = labelDto.getName();
			Label labelInfo = labelRepository.findByName(labelName);
			if (labelInfo == null) {
				// label = modelMapper.map(labelDto, Label.class);
				System.out.println("create label");
				label.setUserLabel(isUserAvailable.get());
				label.setName(labelName);
				label.setLabelId(0);
				return labelRepository.save(label);
			} else {
				log.info("Else Block :");
				throw new LabelAlreadyExistException("Label is already exist...");
			}
		}
		throw new UserNotFoundException("Invalid User Id");
	}

	@Override
	public boolean createOrMapWithNote(LabelDto labelDto, long noteId, String token) {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			String labelName = labelDto.getName();
			LabelDto labelInfo = labelRepository.findOneByName(labelName);
			if (labelInfo == null) {
				log.info("Label is ");
				Label labelDetails = modelMapper.map(labelDto, Label.class);
				labelDetails.setUserLabel(isUserAvailable.get());
				labelRepository.save(labelDetails);

				Optional<Note> noteInfo = noterepository.findById(noteId);
				if (noteInfo.isPresent()) {
					noteInfo.get().getLabels().add(labelDetails);
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
	public boolean removeLabels(String token, long noteId, String labelText) {
		Optional<Note> isNoteAvailable = noterepository.findById(noteId);
		if (isNoteAvailable.isPresent()) {
			isNoteAvailable.get().setLabels(isNoteAvailable.get().getLabels().stream()
					.filter(lbl -> !lbl.getName().equals(labelText)).collect(Collectors.toList()));
			noterepository.save(isNoteAvailable.get());
			return true;

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
				// labelRepository.save(isLabelAvailable.get());
				labelRepository.deleteById(labelId);
				return true;
			}
		}
		return false;
	}

	@Override
	public Label updateLabel(String token, long labelId, LabelDto labelDto) throws UserNotFoundException {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Label> isLabelAvailable = labelRepository.findById(labelId);
			if (isLabelAvailable.isPresent()) {
				isLabelAvailable.get().setName(labelDto.getName());
				return labelRepository.save(isLabelAvailable.get());
			}
		}
		throw new UserNotFoundException("Invalid User");
	}

	@Override
	public List<Label> getAllLabels(String token) {
		long userId = jwtGenerator.parseJWT(token);
		List<Label> labelList = null;
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			labelList = (List<Label>) labelRepository.findAll();
			return labelList;
		}
		return labelList;
	}

	@Override
	public List<Note> getAllNotes(String token, long labelId) {
		long userId = jwtGenerator.parseJWT(token);
		List<Note> noteList = null;
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Label> isLabelAvailable = labelRepository.findById(labelId);
			if (isLabelAvailable.isPresent()) {
				noteList = isLabelAvailable.get().getNoteList();
				log.info("Note List :" + noteList);
				return noteList;
			}
		}
		return noteList;
	}

	@Override
	public boolean addLabels(String token, long noteId, String labelName) {

		Optional<Note> isNoteAvailable = noterepository.findById(noteId);
		if (isNoteAvailable.isPresent()) {
			Label isLabelAvailable = labelRepository.findByName(labelName);
			if (isLabelAvailable != null) {
				log.info("Note :" + isNoteAvailable.get().getTitle());
				/*
				 * isLabelAvailable.get().getNoteList().add(isNoteAvailable.get());
				 * labelRepository.save(isLabelAvailable.get());
				 */
				isNoteAvailable.get().getLabels().add(isLabelAvailable);
				noterepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}
}
