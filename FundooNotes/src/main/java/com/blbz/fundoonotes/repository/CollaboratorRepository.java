package com.blbz.fundoonotes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blbz.fundoonotes.model.Collaborator;
import com.blbz.fundoonotes.model.Note;

@Repository
@Transactional
public interface CollaboratorRepository extends CrudRepository<Collaborator, Long>{

	@Query(value = "select * from collaborator where note_id=?",nativeQuery = true)
	List<Collaborator> getAllColab(long noteId);
	Collaborator findByEmail(String email);
	Collaborator findByEmailAndNoteColab(String email, Note note);
	
	/*
	 * @Query(value =
	 * "delete from collaborator where note_id=? AND email=?",nativeQuery = true)
	 * void deleteByNoteIdAndEmail(long noteId, String email);
	 */
	
	void deleteByNoteColabAndEmail(Note note,String email);

}
