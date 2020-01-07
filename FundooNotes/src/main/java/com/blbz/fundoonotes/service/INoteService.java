package com.blbz.fundoonotes.service;

import java.util.List;

import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.dto.ReminderDto;
import com.blbz.fundoonotes.model.Note;

public interface INoteService {

	boolean computeSave(NoteDto noteDto, String token) throws Exception;

	boolean deleteOneNote(long id, String token) throws Exception;

	boolean isArchived(long id, String token) throws Exception;

	List<Note> getAllNotes(String token) throws Exception;

	boolean addColor(String color, String token, long id) throws Exception;

	boolean pinnedNotes(long id, String token) throws Exception;

	boolean setReminder(long noteId, String token, ReminderDto reminderDto) throws Exception;

	boolean permanentDelete(long noteId, String token) throws Exception;

	List<Note> searchByTitle(String title);

}
