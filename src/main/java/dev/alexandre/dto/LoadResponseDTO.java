package dev.alexandre.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

public class LoadResponseDTO<T> {
	
	public boolean success = true;
	
	public List<T> registros;
	
	@JsonInclude(value=Include.NON_NULL)
	public String msg;
		
}