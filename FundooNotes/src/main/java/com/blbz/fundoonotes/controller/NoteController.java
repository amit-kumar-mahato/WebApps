package com.blbz.fundoonotes.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blbz.fundoonotes.dto.ColorDto;
import com.blbz.fundoonotes.dto.NoteColabDto;
import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.dto.ReminderDto;
import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.Note;
import com.blbz.fundoonotes.responses.Response;
import com.blbz.fundoonotes.service.INoteService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class NoteController {

	@Autowired
	INoteService noteService;

	/*
	 * API to create notes
	 */
	@PostMapping("notes/create")
	@ApiOperation(value = "Api to create new note", response = Response.class)
	public ResponseEntity<Response> createNote(@Valid @RequestBody NoteDto noteDto,
			@RequestHeader("token") String token, BindingResult bindingResult) throws Exception {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(bindingResult.getAllErrors().get(0).getDefaultMessage(), 400));
		}
		Note noteInfo = noteService.computeSave(noteDto, token);
		return noteInfo != null
				? ResponseEntity.status(HttpStatus.OK).body(new Response("Note is created successfully", 200, noteInfo))
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
	}

	/*
	 * Api to add notes into trash
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
	// @JsonIgnore
	@GetMapping("notes")
	@ApiOperation(value = "Api to get notes list", response = Response.class)
	public ResponseEntity<Response> notes(@RequestHeader("token") String token) {
		log.info("GET TOKEN :" + token);
		List<NoteColabDto> notes = noteService.getAllNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Notes are", 200, notes));
	}

	/*
	 * API to make note Archive
	 */
	@PutMapping("notes/archive/{noteId}")
	@ApiOperation(value = "Api to make note archive", response = Response.class)
	public ResponseEntity<Response> makeArchive(@PathVariable("noteId") long noteId,
			@RequestHeader("token") String token) {
		boolean result = noteService.isArchived(noteId, token);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Note is Archived Successfully", 200))
				: ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new Response("Note doesn't Archived", 400));
	}

	/*
	 * API to add color
	 */
	@PutMapping("notes/color/{noteId}")
	@ApiOperation(value = "Api to add color", response = Response.class)
	public ResponseEntity<Response> addColor(@RequestHeader("token") String token,
			@PathVariable("noteId") long noteId,@RequestBody ColorDto colorDto) {
		
		Note result = noteService.addColor(colorDto, token, noteId);
		return (result!=null) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Color is added successfully", 200,result))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}

	/*
	 * API to make note pinned
	 */
	@PutMapping("notes/pin")
	@ApiOperation(value = "Api to make note pinned", response = Response.class)
	public ResponseEntity<Response> pinned(@RequestParam("noteId") long noteId, @RequestHeader("token") String token) {
		boolean result = noteService.pinnedNotes(noteId, token);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Note is pinned", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}

	/*
	 * API to set reminder
	 */
	@PutMapping("notes/reminder")
	@ApiOperation(value = "Api to set remainder", response = Response.class)
	public ResponseEntity<Response> reminder(@RequestParam("noteId") long noteId, @RequestHeader("token") String token,
			@RequestBody ReminderDto reminderDto) throws Exception {
		Note result = noteService.setReminder(noteId, token, reminderDto);
		return (result!=null) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Reminder is Added Successfully", 200,result))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}

	/*
	 * API to delete reminder from note
	 */

	@DeleteMapping("reminder/delete")
	public ResponseEntity<Response> deleteReminder(@RequestParam("noteId") long noteId,
			@RequestHeader("token") String token) {
		return (noteService.deleteReminder(noteId, token)) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Reminder is delted from note", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Id not found", 404));
	}

	/*
	 * API to update note
	 */
	@PutMapping("notes/update")
	@ApiOperation(value = "Api to update note", response = Response.class)
	public ResponseEntity<Response> updateNote(@RequestParam("noteId") long noteId,
			@RequestHeader("token") String token, @RequestBody NoteDto noteDto) {
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is updated successfully", 200,
				noteService.updateNoteDetails(noteId, token, noteDto)));
	}

	/*
	 * API to delete notes permanently
	 */
	@DeleteMapping("notes/permanentDelete/{noteId}")
	@ApiOperation(value = "Api to delete note permanently", response = Response.class)
	public ResponseEntity<Response> permanentDelete(@PathVariable("noteId") long noteId,
			@RequestHeader("token") String token) throws Exception {
		boolean result = noteService.permanentDelete(noteId, token);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Note is deleted permanently", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
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

	/*
	 * API to get all labels of one Note
	 */
//	@GetMapping("notes/labelsofnote")
//	@ApiOperation(value = "Api to get all labels of one note", response = Response.class)
//	public ResponseEntity<Response> getAllLabels(@RequestParam("noteId") long noteId,
//			@RequestHeader("token") String token) {
//		//List<Label> noteList = noteService.getAllLabelsOfOneNote(token, noteId);
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(new Response("Labels releated to current Note are", 200, noteList));
//	}
}
