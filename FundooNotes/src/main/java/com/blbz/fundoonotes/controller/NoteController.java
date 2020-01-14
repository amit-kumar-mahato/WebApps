package com.blbz.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.dto.ReminderDto;
import com.blbz.fundoonotes.model.Note;
import com.blbz.fundoonotes.responses.Response;
import com.blbz.fundoonotes.service.INoteService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiOperation;

@RestController
public class NoteController {

	@Autowired
	INoteService noteService;

	/*
	 * API to create notes
	 */
	@PostMapping("notes/create")
	@ApiOperation(value = "Api to create new note", response = Response.class)
	public ResponseEntity<Response> createNote(@RequestBody NoteDto noteDto, @RequestHeader("token") String token)
			throws Exception {

		boolean result = noteService.computeSave(noteDto, token);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Note is created successfully", 200))
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
	}

	/*
	 * API to delete particular notes
	 */
	@DeleteMapping("notes/delete/{noteId}")
	@ApiOperation(value = "Api to add notes into trash", response = Response.class)
	public ResponseEntity<Response> deleteOneNote(@PathVariable("noteId") long noteId,
			@RequestHeader("token") String token) throws Exception {

		boolean result = noteService.deleteOneNote(noteId, token);
		return (result)
				? ResponseEntity.status(HttpStatus.OK)
						.body(new Response("Note is successfully added to the trashed", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Note ID is not Available", 400));
	}

	/*
	 * API to get all the notes of one user
	 */
	//@JsonIgnore
	@GetMapping("notes")
	@ApiOperation(value = "Api to get notes list", response = Response.class)
	public ResponseEntity<Response> notes(@RequestHeader("token") String token) throws Exception {
		List<Note> notes = noteService.getAllNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Notes are", 200, notes));
	}

	/*
	 * API to make note Archive
	 */
	@PutMapping("notes/archive/{noteId}")
	@ApiOperation(value = "Api to make note archive", response = Response.class)
	public ResponseEntity<Response> makeArchive(@PathVariable("noteId") long noteId,
			@RequestHeader("token") String token){
		boolean result = noteService.isArchived(noteId, token);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Note is Archived Successfully", 200))
				: ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new Response("Note doesn't Archived", 400));
	}

	/*
	 * API to add color
	 */
	@PutMapping("notes/color/{noteId}")
	@ApiOperation(value = "Api to add color", response = Response.class)
	public ResponseEntity<Response> addColor(@RequestParam("color") String color, @RequestHeader("token") String token,
			@PathVariable("noteId") long noteId) {
		boolean result = noteService.addColor(color, token, noteId);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Color is added successfully", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}

	/*
	 * API to make note pinned
	 */
	@PutMapping("notes/pin/{noteId}")
	@ApiOperation(value = "Api to make note pinned", response = Response.class)
	public ResponseEntity<Response> pinned(@PathVariable("noteId") long noteId, @RequestHeader("token") String token)
			throws Exception {
		boolean result = noteService.pinnedNotes(noteId, token);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Note is pinned", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}

	/*
	 * API to set reminder
	 */
	@PutMapping("notes/reminder/{noteId}")
	@ApiOperation(value = "Api to set remainder", response = Response.class)
	public ResponseEntity<Response> reminder(@PathVariable("noteId") long noteId, @RequestHeader("token") String token,
			@RequestBody ReminderDto reminderDto) throws Exception {
		boolean result = noteService.setReminder(noteId, token, reminderDto);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Reminder is Added Successfully", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}

	/*
	 * API to delete notes permanently
	 */
	@DeleteMapping("notes/permanentDelete/{noteId}")
	@ApiOperation(value = "Api to delete note permanently", response = Response.class)
	public ResponseEntity<Response> permanentDelete(@PathVariable("noteId") long noteId,
			@RequestHeader("token") String token) throws Exception {
		boolean result = noteService.permanentDelete(noteId, token);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Note is deleted permanently", 200)) : ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response("The note you are trying to delete is not available", 400));
	}

	/*
	 * API to search note
	 */
	@GetMapping("notes/title")
	@ApiOperation(value = "Api to search note based on title", response = Response.class)
	public ResponseEntity<Response> searchByTitle(@RequestParam("title") String title,
			@RequestHeader("token") String token) {
		List<Note> noteList = noteService.searchByTitle(title);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("List of Notes are", 200, noteList));

	}
}
