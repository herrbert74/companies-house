package com.babestudios.companyinfouk.ui.filinghistorydetails

import android.os.Environment
import android.util.Log

import com.babestudios.companyinfouk.data.DataManager

import net.grandcentrix.thirtyinch.TiPresenter

import javax.inject.Inject

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody

class FilingHistoryDetailsPresenter @Inject
constructor(internal var dataManager: DataManager) : TiPresenter<FilingHistoryDetailsActivityView>(), Observer<ResponseBody> {

	internal var responseBody: ResponseBody? = null

	val isExternalStorageWritable: Boolean
		get() {
			val state = Environment.getExternalStorageState()
			return if (Environment.MEDIA_MOUNTED == state) {
				true
			} else false
		}

	override fun onAttachView(view: FilingHistoryDetailsActivityView) {
		super.onAttachView(view)
		if (responseBody != null) {
			view.checkPermissionAndWritePdf(responseBody!!)
		} else {
			view.showProgress()
		}
	}

	fun getDocument() {
		dataManager.getDocument(view!!.filingHistoryItemString).subscribe(this)
	}

	override fun onComplete() {

	}

	override fun onSubscribe(d: Disposable) {

	}

	override fun onError(e: Throwable) {
		Log.d("test", "onError: " + e.fillInStackTrace())
		if (view != null) {
			view!!.hideProgress()
			view!!.showError()
		}
	}

	override fun onNext(responseBody: ResponseBody) {
		this.responseBody = responseBody
		view!!.checkPermissionAndWritePdf(responseBody)
	}

	internal fun writePdf(responseBody: ResponseBody) {
		this.responseBody = null
		view!!.showDocument(dataManager.writeDocumentPdf(responseBody))
	}


}
