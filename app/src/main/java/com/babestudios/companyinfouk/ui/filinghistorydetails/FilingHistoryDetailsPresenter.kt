package com.babestudios.companyinfouk.ui.filinghistorydetails

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.ObserverWrapper
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.uber.autodispose.AutoDispose
import io.reactivex.CompletableSource
import okhttp3.ResponseBody
import javax.inject.Inject

interface FilingHistoryDetailsPresenterContract : Presenter<FilingHistoryDetailsState, FilingHistoryDetailsViewModel> {
	fun fetchDocument()
	fun writeDocument()
}

@SuppressLint("CheckResult")
class FilingHistoryDetailsPresenter
@Inject
constructor(var companiesRepository: CompaniesRepository) : BasePresenter<FilingHistoryDetailsState, FilingHistoryDetailsViewModel>(), FilingHistoryDetailsPresenterContract {

	override fun setViewModel(viewModel: FilingHistoryDetailsViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		viewModel.state.value?.filingHistoryItem?.description?.let { description ->
			sendToViewModel {
				it.apply {
					this.contentChange = ContentChange.FILING_HISTORY_ITEM_RECEIVED
					this.filingHistoryItemDescription = companiesRepository.filingHistoryLookup(description)
				}
			}
		}
	}

	override fun fetchDocument() {
		viewModel.state.value?.filingHistoryItem?.links?.documentMetadata?.also { data ->
			sendToViewModel {
				it.apply {
					this.isLoading = true
				}
			}
			//val documentId = it.replace("https://document-api.companieshouse.gov.uk/document/", "")

			val documentId = data.replace("https://frontend-doc-api.companieshouse.gov.uk/document/", "")
			companiesRepository.getDocument(documentId)
					.`as`(AutoDispose.autoDisposable(lifeCycleCompletable))
					.subscribeWith(object : ObserverWrapper<ResponseBody>(this) {
						override fun onSuccess(reply: ResponseBody) {
							sendToViewModel {
								it.apply {
									this.isLoading = false
									this.contentChange = ContentChange.PDF_RECEIVED
									this.pdfResponseBody = reply
								}
							}
						}
					})

		}
	}

	override fun writeDocument() {
		viewModel.state.value?.pdfResponseBody?.let { pdfResponseBody ->
			sendToViewModel {
				it.apply {
					this.contentChange = ContentChange.PDF_WRITTEN
					this.pdfUri = companiesRepository.writeDocumentPdf(pdfResponseBody)
				}
			}
		}
	}
}