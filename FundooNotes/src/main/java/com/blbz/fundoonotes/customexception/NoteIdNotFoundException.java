package com.blbz.fundoonotes.customexception;

public class NoteIdNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public NoteIdNotFoundException(String message) {
		super(message);
	}
}
