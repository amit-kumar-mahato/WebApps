package com.blbz.fundoonotes.service;

import java.util.List;

import com.blbz.fundoonotes.customexception.LabelAlreadyExistException;
import com.blbz.fundoonotes.customexception.UserNotFoundException;
import com.blbz.fundoonotes.dto.LabelDto;
import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.Note;

public interface LabelService {

	Label createlabel(String labelname, String token);

	boolean createOrMapWithNote(LabelDto labelDto, long noteId, String token);

	boolean removeLabels(String token, long noteId, String labelText);

	boolean deletepermanently(String token, long labelId);

	Label updateLabel(String token, long labelId, LabelDto labelDto);

	List<Label> getAllLabels(String token);

	List<Note> getAllNotes(String token, long labelId);

	boolean addLabels(String token, long noteId, String labelName);

}
