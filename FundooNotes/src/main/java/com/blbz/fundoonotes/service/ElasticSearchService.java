package com.blbz.fundoonotes.service;

import java.util.List;

import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.model.Note;


public interface ElasticSearchService {

	String CreateNote(Note note);

	String UpdateNote(Note note);

	String DeleteNote(Note note);

	List<Note> searchbytitle(String title);
}
