package com.blbz.fundoonotes.repository;

import org.springframework.data.repository.CrudRepository;

import com.blbz.fundoonotes.model.Note;

public interface NoteRepository extends CrudRepository<Note, Long>{

}
