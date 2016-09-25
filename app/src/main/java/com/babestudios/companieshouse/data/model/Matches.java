package com.babestudios.companieshouse.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Matches {

	@SerializedName("title")
	public List<Integer> title = new ArrayList<>();

	@SerializedName("snippet")
	public List<Object> snippet = new ArrayList<>();

}
