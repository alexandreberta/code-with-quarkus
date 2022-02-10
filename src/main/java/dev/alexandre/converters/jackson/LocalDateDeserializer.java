package dev.alexandre.converters.jackson;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
	
	@Override
	public LocalDate deserialize(JsonParser arg0, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		LocalDate data = null;
		if (arg0.getText() != "") {			
			data = LocalDate.parse(arg0.getText());
		}
		return data;
		
	}
	
}