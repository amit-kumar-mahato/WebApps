package com.blbz.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blbz.fundoonotes.dto.CollaboratorDto;
import com.blbz.fundoonotes.dto.UpdateCollaboratorDto;
import com.blbz.fundoonotes.model.Collaborator;
import com.blbz.fundoonotes.responses.Response;
import com.blbz.fundoonotes.service.ICollaboratorService;

@RestController
@RequestMapping("/collaborator")
public class CollaboratorController {

	@Autowired
	ICollaboratorService collaboratorService;

	/* Api to add Collaborator */
	@PostMapping("/addcollaborator/{noteId}")
	public ResponseEntity<Response> addCollaborator(@RequestBody CollaboratorDto colabDto,
			@PathVariable("noteId") long noteId) {
		Collaborator colabInfo = collaboratorService.addCollaborator(colabDto, noteId);
		return colabInfo != null
				? ResponseEntity.status(HttpStatus.OK)
						.body(new Response("Collaborator Added to the Note", 200, colabInfo))
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("BAD REQUEST", 400));

	}

	/* Api to get all the collaborator of a note */
	@GetMapping("/collaboratorlist")
	public ResponseEntity<Response> getCollaborator(@RequestParam("noteId") long noteId) {
		List<Collaborator> colabList = collaboratorService.getCollaboratorList(noteId);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Collaborator List", 200, colabList));
	}
	
	@PutMapping
	public ResponseEntity<Response> updateCollaborator(@RequestBody UpdateCollaboratorDto updateColabDto){
		collaboratorService.updateCollaborator(updateColabDto);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Successfully updated", 200));
	}

}
