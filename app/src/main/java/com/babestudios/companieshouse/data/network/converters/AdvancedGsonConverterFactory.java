package com.babestudios.companieshouse.data.network.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class AdvancedGsonConverterFactory extends Converter.Factory {

	Gson gson;

	@Override
	public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
															Retrofit retrofit) {
		TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
		return new AdvancedGsonResponseBodyConverter<>(gson, adapter);
	}

	private AdvancedGsonConverterFactory(Gson gson) {
		if (gson == null) throw new NullPointerException("gson == null");
		this.gson = gson;
	}

	public static AdvancedGsonConverterFactory create() {
		return create(new GsonBuilder()//
				.registerTypeAdapter(Date.class, new DateSerializer())//
				.registerTypeAdapter(Boolean.class, new BooleanSerializer())//
				.registerTypeAdapter(boolean.class, new BooleanSerializer())//
				.registerTypeAdapter(String.class, new StringSerializer())//
				.create());
	}

	public static AdvancedGsonConverterFactory create(Gson gson) {
		return new AdvancedGsonConverterFactory(gson);
	}
}
