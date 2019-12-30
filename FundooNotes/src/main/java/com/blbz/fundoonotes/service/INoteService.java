package com.blbz.fundoonotes.service;

import com.blbz.fundoonotes.dto.NoteDto;

public interface INoteService {

	boolean computeSave(NoteDto noteDto, String token) throws Exception;

	boolean deleteOneNote(long id, String token) throws Exception;

}
