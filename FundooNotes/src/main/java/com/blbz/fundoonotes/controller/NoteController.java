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
import com.blbz.fundoonotes.model.Note;
import com.blbz.fundoonotes.repository.NoteRepository;
import com.blbz.fundoonotes.responses.Response;
import com.blbz.fundoonotes.service.INoteService;

@RestController
public class NoteController {

	@Autowired
	INoteService noteService;

	/*
	 * API to create notes
	 */
	@PostMapping("notes/create")
	public ResponseEntity<Response> createNote(@RequestBody NoteDto noteDto, @RequestHeader("token") String token)
			throws Exception {

		boolean result = noteService.computeSave(noteDto, token);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is created successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	/*
	 * API to delete particular notes
	 */
	@DeleteMapping("notes/delete/{noteId}")
	public ResponseEntity<Response> deleteOneNote(@PathVariable("noteId") long noteId, @RequestHeader("token") String token)
			throws Exception {

		boolean result = noteService.deleteOneNote(noteId, token);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new Response("Note is successfully added to the trashed", 200));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Note ID is not Available", 400));
		}
	}

	/*
	 * API to get all the notes of one user
	 */
	@GetMapping("notes")
	public ResponseEntity<Response> notes(@RequestHeader("token") String token) throws Exception {
		List<Note> notes = noteService.getAllNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Notes are", 200, notes));
	}

	/*
	 * API to make note Archive
	 */
	@PutMapping("notes/archive/{noteId}")
	public ResponseEntity<Response> makeArchive(@PathVariable("noteId") long noteId, @RequestHeader("token") String token)
			throws Exception {
		boolean result = noteService.isArchived(noteId, token);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is Archived Successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new Response("Note doesn't Archived", 400));
		}
	}

	/*
	 * API to add color
	 */
	@PutMapping("notes/color/{noteId}")
	public ResponseEntity<Response> addColor(@RequestParam("color") String color, @RequestHeader("token") String token,
			@PathVariable("noteId") long noteId) throws Exception {
		boolean result = noteService.addColor(color, token, noteId);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Color is added successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
		}
	}

	/*
	 * API to make note pinned
	 */
	@PutMapping("notes/pin/{noteId}")
	public ResponseEntity<Response> pinned(@PathVariable("noteId") long noteId, @RequestHeader("token") String token) throws Exception{
		boolean result = noteService.pinnedNotes(noteId,token);
		if(result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is pinned", 200));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
		}
	}
}
