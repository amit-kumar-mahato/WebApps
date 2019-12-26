package com.blbz.fundoonotes.responses;

import org.springframework.stereotype.Component;

@Component
public class MailResponse {

	public String formMessage(String url,String token)
	{
		return url+"/"+token;	
	}
}
