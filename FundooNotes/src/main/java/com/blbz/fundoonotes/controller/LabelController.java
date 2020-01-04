package com.blbz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blbz.fundoonotes.dto.LabelDto;
import com.blbz.fundoonotes.responses.Response;
import com.blbz.fundoonotes.service.LabelService;

@RestController
public class LabelController {

	@Autowired
	LabelService labelService;

	/*
	 * API to create label for note
	 */
	@PostMapping("create/label")
	public ResponseEntity<Response> createLabel(@RequestBody LabelDto labelDto, @RequestHeader("token") String token)
			throws Exception {

		boolean result = labelService.createlabel(labelDto, token);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is Created", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("User doesn't Found", 200));
	}

	/*
	 * API to map exit label to the note
	 */
	@PostMapping("maplabel")
	public ResponseEntity<Response> labelMapToNote(@RequestBody LabelDto labelDto, @RequestParam("noteId") long noteId,
			@RequestHeader("token") String token) throws Exception {
		boolean result = labelService.createOrMapWithNote(labelDto, noteId, token);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Labe is created", 200)) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}
}
