package dev.alexandre.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;

public class ResponseDTO<T> {
	
	public boolean success = true;
	
	@JsonInclude(value=Include.NON_NULL)
	public T registro = null;
	
	@JsonInclude(value=Include.NON_NULL)
	public String msg;

	@JsonInclude(value=Include.NON_EMPTY)
	public List<CampoErroDTO> errors = new ArrayList<>();
	
}
