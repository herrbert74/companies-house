package com.babestudios.companyinfouk.data.network.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class StringSerializer implements JsonSerializer<Boolean>, JsonDeserializer<String> {

	@Override
	public JsonElement serialize(Boolean arg0, Type arg1, JsonSerializationContext arg2) {
		return new JsonPrimitive(arg0);
	}

	@Override
	public String deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		if (arg0.isJsonPrimitive()) {
			if (arg0.getAsJsonPrimitive().isJsonNull()) {
				return "Unknown";
			} else {
				return arg0.getAsString();
			}
		}
		return "Unknown";
	}
}
