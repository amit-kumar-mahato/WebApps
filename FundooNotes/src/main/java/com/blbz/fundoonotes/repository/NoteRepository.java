package com.blbz.fundoonotes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.blbz.fundoonotes.model.Note;

public interface NoteRepository extends CrudRepository<Note, Long>{

	@Query(value = "SELECT * FROM note where user_id=? AND is_trashed=false", 
			  nativeQuery = true)
	List<Note> getAllNotes(long id);

}
