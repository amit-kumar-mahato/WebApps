package com.blbz.fundoonotes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.blbz.fundoonotes.model.Collaborator;

@Repository
public interface CollaboratorRepository extends CrudRepository<Collaborator, Long>{

}
