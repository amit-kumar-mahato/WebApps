package com.blbz.fundoonotes.service;

import com.blbz.fundoonotes.dto.LabelDto;

public interface LabelService {

	boolean createlabel(LabelDto labelDto, String token) throws Exception;

	boolean createOrMapWithNote(LabelDto labelDto, long noteId, String token) throws Exception;

}
