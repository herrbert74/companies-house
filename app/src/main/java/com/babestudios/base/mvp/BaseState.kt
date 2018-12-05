package com.babestudios.base.mvp

import com.babestudios.base.ext.mvp.ErrorType

open class BaseState {
	open var errorType: ErrorType = ErrorType.NONE
	var errorMessage: String? = null
	open var isLoading: Boolean = false
}