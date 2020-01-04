package com.blbz.fundoonotes.repository;

import org.springframework.data.repository.CrudRepository;

import com.blbz.fundoonotes.dto.LabelDto;
import com.blbz.fundoonotes.model.Label;

public interface LabelRepository extends CrudRepository<Label, Long>{

	LabelDto findOneByName(String labelName);

}
