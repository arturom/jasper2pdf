package com.github.arturom.jasper2pdf;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

import net.sf.jasperreports.engine.JRParameter;

public class ParamsReader {
	public static Map<String, Object> parseFile(File jsonFie) throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();
		final MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
		final Map<String, Object> data = mapper.readValue(jsonFie, type);
		return data;
	}
	
	public static Map<String, Object> fixParams(Map <String, Object> params, JRParameter[] reportParams) {
		final HashMap<String, Object> newParams = new HashMap<String, Object>();
		for (JRParameter p : reportParams) {
			final String name = p.getName();
			final Class<?> valueClass = p.getValueClass();
			if (!params.containsKey(name)) {
				continue;
			}
			Object paramValue = params.get(name);
			if (valueClass == Date.class && Long.class.isInstance(paramValue)) {
				paramValue = new Date((Long) paramValue);
			}
			newParams.put(name, paramValue);
		}
		return newParams;
	}
}
