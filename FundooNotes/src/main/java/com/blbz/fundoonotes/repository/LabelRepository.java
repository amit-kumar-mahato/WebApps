package com.blbz.fundoonotes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.blbz.fundoonotes.dto.LabelDto;
import com.blbz.fundoonotes.model.Label;

public interface LabelRepository extends CrudRepository<Label, Long> {

	LabelDto findOneByName(String labelName);
	

	@Query(value = "Select * from label where name=?", nativeQuery = true)
	Label findByName(String labelName);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM label_note where label_id=?", nativeQuery = true)
	void deleteMapping(long labelId);

	@Query(value = "select name from label LEFT JOIN label_note on label_note.label_id = label.label_id where label_note.note_id=?", nativeQuery = true)
	List<String> getLabelsByNoteId(long noteId);

}
