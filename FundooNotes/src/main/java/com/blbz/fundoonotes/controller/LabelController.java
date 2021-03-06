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

import com.blbz.fundoonotes.customexception.LabelAlreadyExistException;
import com.blbz.fundoonotes.customexception.UserNotFoundException;
import com.blbz.fundoonotes.dto.LabelDto;
import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.Note;
import com.blbz.fundoonotes.responses.Response;
import com.blbz.fundoonotes.service.LabelService;

import io.swagger.annotations.ApiOperation;

@RestController
public class LabelController {

	@Autowired
	LabelService labelService;

	/*
	 * API to create label for user
	 */
	@PostMapping("labels/create")
	@ApiOperation(value = "Api to create user label", response = Response.class)
	public ResponseEntity<Response> createLabel(@RequestParam String labelName, @RequestHeader("token") String token) throws LabelAlreadyExistException, UserNotFoundException{
		System.out.println("create label controller");
		System.out.println(labelName);
		Label result = labelService.createlabel(labelName, token);
		return (result!=null) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is Created", 200,result))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Label is Already exist", 400));

	}

	/*
	 * API to map exit label to the note
	 */
	@PostMapping("labels/create/{noteId}")
	public ResponseEntity<Response> labelMapToNote(@RequestBody LabelDto labelDto, @PathVariable("noteId") long noteId,
			@RequestHeader("token") String token){
		boolean result = labelService.createOrMapWithNote(labelDto, noteId, token);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is created successfully", 200))
				: ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new Response("The label you are trying to create is already exist!!!", 400));
	}

	/*
	 * API to Remove mapping of label from notes
	 */
	@DeleteMapping("labels/remove")
	@ApiOperation(value = "Api to remove label from note", response = Response.class)
	public ResponseEntity<Response> removeLabel(@RequestHeader("token") String token,
			@RequestParam("noteId") long noteId, @RequestParam("labelText") String labelText) {
		boolean result = labelService.removeLabels(token, noteId, labelText);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is Removed Successfully", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}

	/*
	 * API to delete labels permanently
	 */
	@DeleteMapping("labels/delete")
	@ApiOperation(value = "Api to delete label", response = Response.class)
	public ResponseEntity<Response> deletelabel(@RequestHeader("token") String token,
			@RequestParam("labelId") long labelId) throws Exception {
		boolean result = labelService.deletepermanently(token, labelId);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is deleted successfully", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new Response("The label you are trying to delete is not Available", 400));
	}

	/*
	 * API to update the label
	 */
	@PutMapping("labels/update")
	@ApiOperation(value = "Api to update label", response = Response.class)
	public ResponseEntity<Response> updateLabel(@RequestHeader("token") String token,
			@RequestParam("labelId") long labelId, @RequestBody LabelDto labelDto) {
		Label result = labelService.updateLabel(token, labelId, labelDto);
		return (result!=null) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is updated successfully", 200,result))
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new Response("The label you are trying to update is not available", 400));
	}

	/*
	 * API to get All Labels
	 */
	@GetMapping("labels")
	@ApiOperation(value = "Api to get all labels", response = Response.class)
	public ResponseEntity<Response> getAllLabels(@RequestHeader("token") String token) throws Exception {
		List<Label> labelList = labelService.getAllLabels(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Label List are", 200, labelList));
	}

	/*
	 * API to get all notes with same labels
	 */
	@GetMapping("labels/getNotes")
	@ApiOperation(value = "Api to get all notes releated to one label", response = Response.class)
	public ResponseEntity<Response> getAllNotes(@RequestHeader("token") String token,
			@RequestParam("labelId") long labelId) throws Exception {
		List<Note> noteList = labelService.getAllNotes(token, labelId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new Response("Notes releated to current labelId are", 200, noteList));
	}

	/*
	 * API to add labels with notes
	 */
	@PostMapping("addlabels")
	@ApiOperation(value = "Api to add existing label with note", response = Response.class)
	public ResponseEntity<Response> addLabelToNotes(@RequestHeader("token") String token,
			@RequestParam("noteId") long noteId, @RequestParam("labelName") String labelName) {
		boolean result = labelService.addLabels(token, noteId, labelName);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is added to the notes", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}
}
