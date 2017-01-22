/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.babestudios.companyinfouk.data.network.converters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class AdvancedGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
	private final Gson gson;
	private final TypeAdapter<T> adapter;

	AdvancedGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
		this.gson = gson;
		this.adapter = adapter;
	}

	@Override
	public T convert(ResponseBody body) throws IOException {
		String dirty = body.string();
		String clean = dirty.replaceAll("finance_charts_json_callback\\(", "");
		clean = clean.replaceAll("\\)$", "");
		try {
			return adapter.fromJson(clean);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			body.close();
		}
	}
}
