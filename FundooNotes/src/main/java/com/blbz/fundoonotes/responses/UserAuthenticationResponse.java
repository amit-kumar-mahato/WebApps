package com.blbz.fundoonotes.responses;

public class UserAuthenticationResponse {

	private String token;
	
	private int statuscode;
	
	private Object obj;
	
	public UserAuthenticationResponse(String token,int statuscode,Object obj)
	{
		this.token=token;
		
		this.statuscode=statuscode;
		
		this.obj=obj;
	}
}
