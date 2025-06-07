package com.babestudios.companyinfouk.shared.data

import com.eygraber.uri.Uri
import io.ktor.utils.io.ByteReadChannel

expect suspend fun ByteReadChannel.writeToFile(uri: Uri)
