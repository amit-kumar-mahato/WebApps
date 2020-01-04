package com.blbz.fundoonotes.customexception;

public class LabelAlreadyExistException extends Exception{

	private static final long serialVersionUID = 1L;

	public LabelAlreadyExistException(String message) {
		super(message);
	}
	
}
