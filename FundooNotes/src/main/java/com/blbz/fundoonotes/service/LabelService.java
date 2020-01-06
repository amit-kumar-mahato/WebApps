package com.blbz.fundoonotes.service;

import java.util.List;

import com.blbz.fundoonotes.dto.LabelDto;
import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.Note;

public interface LabelService {

	boolean createlabel(LabelDto labelDto, String token) throws Exception;

	boolean createOrMapWithNote(LabelDto labelDto, long noteId, String token) throws Exception;

	boolean removeLabels(String token, long noteId, long labelId);

	boolean deletepermanently(String token, long labelId) throws Exception;

	boolean updateLabel(String token, long labelId, LabelDto labelDto)throws Exception;

	List<Label> getAllLabels(String token) throws Exception;

	List<Note> getAllNotes(String token, long labelId) throws Exception;

	boolean addLabels(String token, long noteId, long labelId);

}
