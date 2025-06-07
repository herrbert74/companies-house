package com.babestudios.companyinfouk.shared.data

import com.eygraber.uri.Uri
import io.ktor.utils.io.ByteReadChannel

//private const val BUFFER_SIZE = 4096

//	@OptIn(ExperimentalForeignApi::class)
	actual suspend fun ByteReadChannel.writeToFile(uri: Uri) {
//		val channel = this
//		val queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.convert(), 0u)
//		memScoped {
//			val dst = allocArray<ByteVar>(BUFFER_SIZE)
//			val fd = open(filepath, O_RDWR)
//
//			try {
//				while (!channel.isClosedForRead) {
//					val rs = channel.readAvailable(dst, 0, BUFFER_SIZE)
//					if (rs < 0) break
//
//					val data = dispatch_data_create(dst, rs.convert(), queue) {}
//
//					dispatch_write(fd, data, queue) { _, error ->
//						if (error != 0) {
//							channel.cancel(IllegalStateException("Unable to write data to the file $filepath"))
//						}
//					}
//				}
//			} finally {
//				close(fd)
//			}
//		}
	}
