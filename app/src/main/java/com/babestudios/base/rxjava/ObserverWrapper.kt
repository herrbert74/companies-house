package com.babestudios.base.rxjava

import com.babestudios.base.ext.getErrorMessageFromResponseBody
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.ErrorType
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException


abstract class ObserverWrapper<R>(var presenter: BasePresenter<*, *>) : Observer<R> {

	private var weakReference: WeakReference<BasePresenter<*, *>>? = null

	init {
		weakReference = WeakReference(presenter)
	}

	protected abstract fun onSuccess(reply: R)

	open fun onFailed(e: Throwable) {}


	override fun onComplete() {

	}

	override fun onSubscribe(d: Disposable) {

	}

	override fun onNext(reply: R) {
		onSuccess(reply)
	}

	override fun onError(e: Throwable) {
		val basePresenter = weakReference?.get()
		if (basePresenter != null) {
			when (e) {
				is HttpException -> {
					val responseBody = e.response().errorBody()
					basePresenter.onError(ErrorType.UNKNOWN, responseBody?.getErrorMessageFromResponseBody())
				}
				is SocketTimeoutException -> basePresenter.onError(ErrorType.TIMEOUT, "")
				is IOException -> basePresenter.onError(ErrorType.NETWORK, "")
				else -> basePresenter.onError(ErrorType.UNKNOWN, e.message)
			}
		}
		onFailed(e)
	}
}