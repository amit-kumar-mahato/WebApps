package com.blbz.fundoonotes.service;

import java.util.List;

import com.blbz.fundoonotes.customexception.NoteIdNotFoundException;
import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.dto.ReminderDto;
import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.Note;

public interface INoteService {

	Note computeSave(NoteDto noteDto, String token);

	boolean deleteOneNote(long id, String token) throws NoteIdNotFoundException;

	boolean isArchived(long id, String token);

	List<Note> getAllNotes(String token);

	boolean addColor(String color, String token, long id);

	boolean pinnedNotes(long id, String token);

	boolean setReminder(long noteId, String token, ReminderDto reminderDto);

	boolean permanentDelete(long noteId, String token);

	List<Note> searchByTitle(String title);

	List<Label> getAllLabelsOfOneNote(String token, long noteId);

	Note updateNoteDetails(long noteId, String token, NoteDto noteDto) throws NoteIdNotFoundException;

}
