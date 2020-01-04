package com.blbz.fundoonotes.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/*
@Getter
@Setter*/
public class ReminderDto {

	
	private LocalDateTime time;

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	
}
