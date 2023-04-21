package com.babestudios.companyinfouk.data.utils


import android.util.Base64

class Base64Wrapper {
	fun encodeToString(input: ByteArray, flags: Int): String {
		return Base64.encodeToString(input, flags)
	}
}
