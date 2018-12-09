package com.babestudios.base.mvp

open class BaseState {
	open var errorType: ErrorType = ErrorType.NONE
	var errorMessage: String? = null
	open var isLoading: Boolean = false
}