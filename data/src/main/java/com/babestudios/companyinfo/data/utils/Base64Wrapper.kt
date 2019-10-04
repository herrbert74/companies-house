package com.babestudios.companyinfo.data.utils


import android.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Base64Wrapper @Inject constructor(){
	fun encodeToString(input: ByteArray, flags: Int): String {
		return Base64.encodeToString(input, flags)
	}
}
