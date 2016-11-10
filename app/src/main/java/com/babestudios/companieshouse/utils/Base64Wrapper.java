package com.babestudios.companieshouse.utils;


import android.util.Base64;

public class Base64Wrapper {
	public String encodeToString(byte[] input, int flags) {
		return Base64.encodeToString(input, flags);
	}
}
