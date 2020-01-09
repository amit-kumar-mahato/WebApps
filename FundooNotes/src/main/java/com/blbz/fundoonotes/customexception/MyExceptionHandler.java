package com.blbz.fundoonotes.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.blbz.fundoonotes.responses.Response;

@RestControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(EmailAlreadyExistException.class)
	public ResponseEntity<Response> handleSpecificException(Exception ex){
		Response response = new Response(ex.getMessage(), 400);
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserNotVerifiedException.class)
	public ResponseEntity<Response> userNotVerified(Exception ex){
		Response response = new Response(ex.getMessage(), 404);
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
}
