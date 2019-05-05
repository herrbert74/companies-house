package com.babestudios.companyinfouk.ui.filinghistorydetails

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.babestudios.base.ext.biLet
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.Injector
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.filinghistory.FilingHistoryPresenter
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_filing_history_details.*
import kotlinx.android.synthetic.main.multi_state_view_error.view.*
import java.util.*

private const val FILING_HISTORY_ITEM = "com.babestudios.companyinfouk.ui.filing_history_item"
private const val REQUEST_WRITE_STORAGE = 1687

class FilingHistoryDetailsActivity : RxAppCompatActivity(), ScopeProvider {


	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(FilingHistoryDetailsViewModel::class.java) }

	private lateinit var filingHistoryDetailsPresenter: FilingHistoryDetailsPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_filing_history_details)
		logScreenView(this.localClassName)
		setSupportActionBar(pabFilingHistoryDetails.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabFilingHistoryDetails.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.filing_history_details)
		when {
			viewModel.state.value.filingHistoryItem != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<FilingHistoryDetailsState>("STATE")?.let {
					with(viewModel.state.value) {
						filingHistoryItem = it.filingHistoryItem
					}
				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value.filingHistoryItem = intent.getParcelableExtra(FILING_HISTORY_ITEM)
				initPresenter(viewModel)
			}
		}
	}

	override fun onResume() {
		super.onResume()
		observeActions()
		observeState()
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		outState?.putParcelable("STATE", viewModel.state.value)
		super.onSaveInstanceState(outState)
	}

	private fun initPresenter(viewModel: FilingHistoryDetailsViewModel) {
		if (!::filingHistoryDetailsPresenter.isInitialized) {
			filingHistoryDetailsPresenter = Injector.get().filingHistoryDetailsPresenter()
			filingHistoryDetailsPresenter.setViewModel(viewModel, requestScope())
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		viewModel.state.value.filingHistoryItem?.links?.documentMetadata?.let {
			menuInflater.inflate(R.menu.filing_history_details_menu, menu)
		}
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.action_show_pdf -> {
				filingHistoryDetailsPresenter.fetchDocument()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun onBackPressed() {
		super.onBackPressed()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	//endregion

	//region Render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: FilingHistoryDetailsState) {
		when {
			state.contentChange == ContentChange.FILING_HISTORY_ITEM_RECEIVED -> {
				state.contentChange = ContentChange.NONE
				(state.filingHistoryItem to state.filingHistoryItemDescription).biLet { item, description ->
					msvFilingHistoryDetails.viewState = VIEW_STATE_CONTENT
					showFilingHistoryItem(item, description)
				}
			}
			state.isLoading -> {
				msvFilingHistoryDetails.viewState = VIEW_STATE_LOADING
			}
			state.contentChange == ContentChange.PDF_RECEIVED -> {
				state.contentChange = ContentChange.NONE
				checkPermissionAndWriteDocument()
			}
			state.contentChange == ContentChange.PDF_WRITTEN -> {
				state.contentChange = ContentChange.NONE
				msvFilingHistoryDetails.viewState = VIEW_STATE_CONTENT
				viewModel.state.value.pdfUri?.let {
					showDocument(it)
				}
			}
			state.errorType != ErrorType.NONE -> {
				msvFilingHistoryDetails.viewState = VIEW_STATE_ERROR
				state.errorType = ErrorType.NONE
				msvFilingHistoryDetails.tvMsvError.text = state.errorMessage
			}
			state.filingHistoryItem == null -> {
				msvFilingHistoryDetails.viewState = VIEW_STATE_EMPTY
			}
		}
	}

	private fun showFilingHistoryItem(filingHistoryItem: FilingHistoryItem, filingHistoryItemDescription: String) {
		textViewDate?.text = filingHistoryItem.date
		textViewCategory?.text = filingHistoryItem.category?.displayName
		if (filingHistoryItem.subcategory != null) {
			textViewSubcategory?.text = filingHistoryItem.subcategory
		} else {
			textViewSubcategory?.visibility = View.GONE
			textViewLabelSubcategory?.visibility = View.GONE
		}
		textViewDescription?.text = filingHistoryItem.description
		if (filingHistoryItem.description == "legacy" || filingHistoryItem.description == "miscellaneous") {
			textViewDescription?.text = filingHistoryItem.descriptionValues?.description
		} else {
			filingHistoryItem.description?.let {
				val spannableDescription = FilingHistoryPresenter.createSpannableDescription(filingHistoryItemDescription, filingHistoryItem)
				textViewDescription?.text = spannableDescription
			}
		}

		if (filingHistoryItem.pages != null) {
			textViewPages?.text = String.format(Locale.UK, "%d", filingHistoryItem.pages)
		} else {
			textViewLabelPages?.visibility = View.GONE
		}

		if (filingHistoryItem.category?.displayName == "capital" && filingHistoryItem.descriptionValues?.capital?.isNotEmpty() == true) {
			filingHistoryItem.descriptionValues?.let {
				textViewDescriptionValues?.text = "${it.capital[0].currency} ${it.capital[0].figure}"
			}
		} else {
			textViewDescriptionValues?.visibility = View.GONE
		}
	}

	private fun checkPermissionAndWriteDocument() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			filingHistoryDetailsPresenter.writeDocument()
		} else {
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_STORAGE)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		if (requestCode == REQUEST_WRITE_STORAGE) {
			if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				filingHistoryDetailsPresenter.writeDocument()
			} else {
				Toast.makeText(this, getString(R.string.filing_history_details_wont_save_pdf), Toast.LENGTH_LONG).show()
			}
		}
	}

	private fun showDocument(pdfBytes: Uri) {
		val target = Intent(Intent.ACTION_VIEW)
		target.setDataAndType(pdfBytes, "application/pdf")
		target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
		val intent = Intent.createChooser(target, "Open File")
		try {
			startActivityWithRightSlide(intent)
		} catch (e: ActivityNotFoundException) {
			Toast.makeText(this, getString(R.string.filing_history_details_no_pdf_reader), Toast.LENGTH_LONG).show()
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
	}

	//endregion
}

fun Context.createFilingHistoryDetailsIntent(filingHistoryItem: FilingHistoryItem): Intent {
	return Intent(this, FilingHistoryDetailsActivity::class.java)
			.putExtra(FILING_HISTORY_ITEM, filingHistoryItem)
}
