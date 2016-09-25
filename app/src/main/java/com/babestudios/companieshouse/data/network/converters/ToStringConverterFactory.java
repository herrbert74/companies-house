package com.babestudios.companieshouse.data.network.converters;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ToStringConverterFactory extends Converter.Factory {
	private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

	@Override
	public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
		if (String.class.equals(type)) {
			return value -> value.string();
		}
		return null;
	}

	@Override
	public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
		if (String.class.equals(type)) {
			return new Converter<String, RequestBody>() {
				@Override
				public RequestBody convert(String value) throws IOException {
					return RequestBody.create(MEDIA_TYPE, value);
				}
			};
		}
		return null;
	}

	public static ToStringConverterFactory create() {
		return new ToStringConverterFactory();
	}
}
