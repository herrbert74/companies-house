package com.babestudios.companyinfouk.filings.ui.details

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.view.MviView
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.internal.PublishSubject
import com.babestudios.base.ext.viewBinding
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_CONTENT
import com.babestudios.companyinfouk.common.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.filings.databinding.FragmentFilingHistoryDetailsBinding
import com.babestudios.companyinfouk.filings.ui.FilingsViewModel
import com.babestudios.companyinfouk.filings.ui.FilingsViewModelFactory
import com.babestudios.companyinfouk.filings.ui.createSpannableDescription
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStore.State
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

private const val REQUEST_WRITE_STORAGE = 1687
private const val REQUEST_CREATE_DOCUMENT = 1688

@AndroidEntryPoint
class FilingHistoryDetailsFragment : Fragment(R.layout.fragment_filing_history_details), MviView<State, UserIntent> {

	@Inject
	lateinit var filingsViewModelFactory: FilingsViewModelFactory

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	private val args: FilingHistoryDetailsFragmentArgs by navArgs()

	private val viewModel: FilingsViewModel by viewModels {
		FilingsViewModel.provideFactory(
			filingsViewModelFactory,
			args.selectedCompanyId
		)
	}

	private val binding by viewBinding<FragmentFilingHistoryDetailsBinding>()

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			findNavController().popBackStack()
		}
	})

	private lateinit var documentId: String

	/**
	 * This flag is to prevent calling create document twice when invalidate is called after coming back from Platform
	 * File Chooser, but there is no state change
	 */
	private var wasCreateDocumentCalled: Boolean = false

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

	/**
	 * Dispatches the provided `View Event` to all subscribers
	 *
	 * @param event a `View Event` to be dispatched
	 */
	@MainThread
	fun dispatch(event: UserIntent) {
		subject.onNext(event)
	}

	override fun render(model: State) {
		when (model) {
			is State.DocumentDownloaded -> {
				binding.msvFilingHistoryDetails.viewState = VIEW_STATE_CONTENT
				if (!wasCreateDocumentCalled) {
					checkPermissionAndWriteDocument()
					wasCreateDocumentCalled = true
				}
			}
			is State.DocumentWritten -> {
				wasCreateDocumentCalled = false
				binding.msvFilingHistoryDetails.viewState = VIEW_STATE_CONTENT
				showDocument(model.uri)
			}
			is State.Error -> {}
			is State.Show -> {}
		}

	}

	//endregion


	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		documentId = args.selectedFilingsItem.links.documentMetadata.substringAfterLast("/")
		viewModel.onViewCreated(this, essentyLifecycle(), args.selectedFilingsItem)
		initializeToolBar()
		showFilingHistoryItem()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
	}

	private fun initializeToolBar() {
		companiesRepository.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabFilingHistoryDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabFilingHistoryDetails.setNavigationOnClickListener { findNavController().popBackStack() }
		toolBar?.setTitle(R.string.filing_history_details)
	}

	override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
		menuInflater.inflate(R.menu.filing_history_details_menu, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.action_show_pdf -> {
				dispatch(UserIntent.FetchDocument(documentId))
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	//endregion

	private fun showFilingHistoryItem() {

		val filingHistoryItem = args.selectedFilingsItem
		binding.lblFilingHistoryDetailsDate.text = filingHistoryItem.date
		binding.lblFilingHistoryDetailsCategory.text = filingHistoryItem.category.displayName
		if (filingHistoryItem.subcategory.isEmpty()) {
			binding.lblFilingHistoryDetailsSubcategory.visibility = GONE
			binding.cpnFilingHistoryDetailsSubcategory.visibility = GONE
		} else {
			binding.lblFilingHistoryDetailsSubcategory.text = filingHistoryItem.subcategory
		}
		binding.lblFilingHistoryDetailsDescription.text = filingHistoryItem.description
		filingHistoryItem.description.let {
			val spannableDescription = filingHistoryItem
				.description
				.createSpannableDescription()
			binding.lblFilingHistoryDetailsDescription.text = spannableDescription
		}
		binding.lblFilingHistoryDetailsPages.text = String.format(Locale.UK, "%d", filingHistoryItem.pages)
		binding.lblFilingHistoryDetailsDescriptionValues.visibility = GONE

	}

	private fun checkPermissionAndWriteDocument() {
		if (ContextCompat.checkSelfPermission(
				requireContext(),
				Manifest.permission.WRITE_EXTERNAL_STORAGE
			) == PackageManager.PERMISSION_GRANTED) {
			createDocument()
		} else {
			requestPermissions(
				arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
				REQUEST_WRITE_STORAGE
			)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		if (requestCode == REQUEST_WRITE_STORAGE) {
			if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				createDocument()
			} else {
				Toast.makeText(
					requireContext(),
					getString(R.string.filing_history_details_wont_save_pdf),
					Toast.LENGTH_LONG
				).show()
			}
		}
	}

	private fun createDocument() {
		val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
			.setType("application/pdf")
			.putExtra(Intent.EXTRA_TITLE, "${documentId}.pdf")
			.addCategory(Intent.CATEGORY_OPENABLE)
		startActivityForResult(intent, REQUEST_CREATE_DOCUMENT)
	}

	override fun onActivityResult(
		requestCode: Int,
		resultCode: Int,
		intent: Intent?
	) {
		if (requestCode == REQUEST_CREATE_DOCUMENT) {
			if (resultCode == RESULT_OK && intent != null) {
				intent.data?.let { uri ->
					dispatch(UserIntent.WriteDocument(uri))
				}
			}
		} else {
			super.onActivityResult(requestCode, resultCode, intent)
		}
	}

	@Suppress("SwallowedException")
	private fun showDocument(pdfBytes: Uri) {
		val target = Intent(Intent.ACTION_VIEW)
		target.setDataAndType(pdfBytes, "application/pdf")
		target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
		val intent = Intent.createChooser(target, "Open File")
		try {
			(requireActivity() as AppCompatActivity).startActivityWithRightSlide(intent)
		} catch (e: ActivityNotFoundException) {
			Toast.makeText(
				requireContext(),
				getString(R.string.filing_history_details_no_pdf_reader),
				Toast.LENGTH_LONG
			).show()
		}
	}

	//endregion

}

sealed class UserIntent {
	data class FetchDocument(val documentId: String) : UserIntent()
	data class WriteDocument(val uri: Uri) : UserIntent()
}
