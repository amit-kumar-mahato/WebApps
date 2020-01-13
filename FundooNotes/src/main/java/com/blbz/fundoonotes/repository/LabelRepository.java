package com.blbz.fundoonotes.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.blbz.fundoonotes.dto.LabelDto;
import com.blbz.fundoonotes.model.Label;

public interface LabelRepository extends CrudRepository<Label, Long>{

	LabelDto findOneByName(String labelName);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM label_note where label_id=?", nativeQuery = true)
	void deleteMapping(long labelId);

}
