package com.babestudios.companyinfouk.ui.filinghistorydetails

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import net.grandcentrix.thirtyinch.TiPresenter
import okhttp3.ResponseBody
import javax.inject.Inject

class FilingHistoryDetailsPresenter @Inject
constructor(internal var companiesRepository: CompaniesRepository) : TiPresenter<FilingHistoryDetailsActivityView>(), Observer<ResponseBody> {

	private var responseBody: ResponseBody? = null

	override fun onAttachView(view: FilingHistoryDetailsActivityView) {
		super.onAttachView(view)
		responseBody?.let {
			view.checkPermissionAndWritePdf(it)
		} ?: run {
			view.showProgress()
		}
	}

	fun getDocument(filingHistoryItemString: String) {
		val gson = Gson()
		val filingHistoryItem = gson.fromJson(filingHistoryItemString, FilingHistoryItem::class.java)
		filingHistoryItem.links?.documentMetadata?.let {
			//val documentId = it.replace("https://document-api.companieshouse.gov.uk/document/", "")
			val documentId = it.replace("https://frontend-doc-api.companieshouse.gov.uk/document/", "")
			companiesRepository.getDocument(documentId).subscribe(this)
		}
	}

	override fun onComplete() {

	}

	override fun onSubscribe(d: Disposable) {

	}

	override fun onError(e: Throwable) {
		view?.hideProgress()
		view?.showError()
	}

	override fun onNext(responseBody: ResponseBody) {
		this.responseBody = responseBody
		view?.checkPermissionAndWritePdf(responseBody)
	}

	internal fun writePdf(responseBody: ResponseBody) {
		this.responseBody = null
		view?.showDocument(companiesRepository.writeDocumentPdf(responseBody))
	}


}
