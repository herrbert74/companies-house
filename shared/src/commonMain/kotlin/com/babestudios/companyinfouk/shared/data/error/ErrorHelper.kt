package com.babestudios.companyinfouk.shared.data.error

import com.babestudios.companyinfouk.shared.domain.ErrorMaps

/**
 * Not used as CH API does not send handleable exceptions
 */
class ErrorHelper {

	fun errorLookUp(errorString: String): String {
		return ErrorMaps.service[errorString] ?: errorString
	}

}
