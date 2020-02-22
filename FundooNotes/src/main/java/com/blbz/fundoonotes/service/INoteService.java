package com.blbz.fundoonotes.service;

import java.util.List;

import com.blbz.fundoonotes.customexception.NoteIdNotFoundException;
import com.blbz.fundoonotes.customexception.UserNotFoundException;
import com.blbz.fundoonotes.dto.ColorDto;
import com.blbz.fundoonotes.dto.NoteColabDto;
import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.dto.ReminderDto;
import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.Note;

public interface INoteService {

	Note computeSave(NoteDto noteDto, String token);

	boolean deleteOneNote(long id, String token) throws NoteIdNotFoundException;

	boolean isArchived(long id, String token);

	List<NoteColabDto> getAllNotes(String token);

	Note addColor(ColorDto colorDto, String token, long id);

	boolean pinnedNotes(long id, String token);

	Note setReminder(long noteId, String token, ReminderDto reminderDto) throws UserNotFoundException;

	boolean permanentDelete(long noteId, String token);

	List<Note> searchByTitle(String title);

	Note updateNoteDetails(long noteId, String token, NoteDto noteDto) throws NoteIdNotFoundException;

	boolean deleteReminder(long noteId, String token);

}
