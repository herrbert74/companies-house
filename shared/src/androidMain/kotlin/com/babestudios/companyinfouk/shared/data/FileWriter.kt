package com.babestudios.companyinfouk.shared.data

import com.eygraber.uri.Uri
import com.eygraber.uri.toAndroidUri
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import java.io.FileOutputStream
import kotlinx.io.readByteArray
import okhttp3.internal.platform.PlatformRegistry.applicationContext

actual suspend fun ByteReadChannel.writeToFile(uri: Uri) {

	val contentResolver = applicationContext?.contentResolver
	contentResolver?.openFileDescriptor(uri.toAndroidUri(), "w")?.use {
		FileOutputStream(it.fileDescriptor).use { fileOutputStream ->
			fileOutputStream.write(
				this.readRemaining().readByteArray()
			)
		}
	}

}
