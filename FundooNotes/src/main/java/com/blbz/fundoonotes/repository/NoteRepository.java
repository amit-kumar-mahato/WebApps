package com.blbz.fundoonotes.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.Note;

@Transactional
public interface NoteRepository extends CrudRepository<Note, Long> {

	@Query(value = "SELECT * FROM note where user_id=?", nativeQuery = true)
	List<Note> getAllNotes(long id);

	@Modifying
	@Query(value = "DELETE FROM note where note_id=? AND is_trash=true", nativeQuery = true)
	void deleteNotesPermanently(long noteId);

	@Query(name="tofindlabelfornote", nativeQuery = true)
	List<Label> getLabelsByNoteId(long noteId);

}
