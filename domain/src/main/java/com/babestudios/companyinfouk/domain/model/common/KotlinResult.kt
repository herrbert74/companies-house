package com.babestudios.companyinfouk.domain.model.common

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching

typealias ApiResult<T> = Result<T, Throwable>

inline fun <T, V> T.apiRunCatching(block: T.() -> V) = runCatching(block)
