package com.blbz.fundoonotes.service;

import java.util.List;

import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.model.Note;


public interface ElasticSearchService {

	String createNote(Note note);

	List<Note> searchByTitle(String title);
}
