package com.blbz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.responses.Response;
import com.blbz.fundoonotes.service.INoteService;

@RestController
public class NoteController {
	
	@Autowired
	INoteService noteService;
	
	/*
	 * API to create notes
	 * */
	@PostMapping("notes/create")
	public ResponseEntity<Response> createNote(@RequestBody NoteDto noteDto, @RequestHeader("token") String token) throws Exception{
		
		boolean result = noteService.computeSave(noteDto, token);
		if(result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is created successfully", 200));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}
	
	/*
	 * API to delete particular notes
	 * */
	@DeleteMapping("notes/delete/{id}")
	public ResponseEntity<Response> deleteOneNote(@PathVariable("id") long id, @RequestHeader("token") String token) throws Exception{
		
		boolean result = noteService.deleteOneNote(id,token);
		return null;
	}
}
