package com.babestudios.companyinfouk.filings.ui.details

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.mvrx.BaseFragment
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.common.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.filings.databinding.FragmentFilingHistoryDetailsBinding
import com.babestudios.companyinfouk.filings.ui.FilingsActivity
import com.babestudios.companyinfouk.filings.ui.FilingsViewModel
import com.babestudios.companyinfouk.filings.ui.createSpannableDescription
import io.reactivex.disposables.CompositeDisposable
import java.util.*

private const val REQUEST_WRITE_STORAGE = 1687
private const val REQUEST_CREATE_DOCUMENT = 1688

class FilingHistoryDetailsFragment : BaseFragment() {

	/**
	 * This flag is to prevent calling create document twice when invalidate is called after coming back from Platform
	 * File Chooser, but there is no state change
	 */
	private var wasCreateDocumentCalled: Boolean = false
	private val viewModel by existingViewModel(FilingsViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentFilingHistoryDetailsBinding? = null
	private val binding get() = _binding!!

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			viewModel.filingsNavigator.popBackStack()
		}
	})

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		_binding = FragmentFilingHistoryDetailsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	override fun orientationChanged() {
		val activity = requireActivity() as FilingsActivity
		viewModel.setNavigator(activity.injectFilingsNavigator())
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
		_binding = null
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabFilingHistoryDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabFilingHistoryDetails.setNavigationOnClickListener { viewModel.filingsNavigator.popBackStack() }
		toolBar?.setTitle(R.string.filing_history_details)
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
		withState(viewModel) { state ->
			state.filingHistoryItem.links.documentMetadata.run {
				menuInflater.inflate(R.menu.filing_history_details_menu, menu)
			}
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.action_show_pdf -> {
				viewModel.fetchDocument()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	//endregion

	//region Render

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.documentRequest) {
				is Uninitialized -> {
					binding.msvFilingHistoryDetails.viewState = VIEW_STATE_CONTENT
					showFilingHistoryItem()
				}
				is Loading -> {
					binding.msvFilingHistoryDetails.viewState = VIEW_STATE_LOADING
				}
				is Success -> {
					when (state.writeDocumentRequest) {
						is Uninitialized -> {
							binding.msvFilingHistoryDetails.viewState = VIEW_STATE_CONTENT
							if (!wasCreateDocumentCalled) {
								checkPermissionAndWriteDocument()
								wasCreateDocumentCalled = true
							} else
								return@withState
						}
						is Loading -> {
							binding.msvFilingHistoryDetails.viewState = VIEW_STATE_LOADING
						}
						is Success -> {
							wasCreateDocumentCalled = false
							binding.msvFilingHistoryDetails.viewState = VIEW_STATE_CONTENT
							state.pdfUri?.let {
								viewModel.resetState()
								showDocument(it)
							}
						}
						is Fail -> {
							binding.msvFilingHistoryDetails.viewState = VIEW_STATE_ERROR
							val tvMsvError = binding.msvFilingHistoryDetails.findViewById<TextView>(R.id.tvMsvError)
							tvMsvError.text = state.writeDocumentRequest.error.message
						}
					}
				}
				is Fail -> {
					binding.msvFilingHistoryDetails.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvFilingHistoryDetails.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.documentRequest.error.message
				}
			}
		}
	}

	private fun showFilingHistoryItem() {
		withState(viewModel) { state ->
			val filingHistoryItem = state.filingHistoryItem
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
		withState(viewModel) {
			val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
					.setType("application/pdf")
					.putExtra(Intent.EXTRA_TITLE, "${it.documentId}.pdf")
					.addCategory(Intent.CATEGORY_OPENABLE)
			startActivityForResult(intent, REQUEST_CREATE_DOCUMENT)
		}
	}

	override fun onActivityResult(
			requestCode: Int,
			resultCode: Int,
			intent: Intent?
	) {
		if (requestCode == REQUEST_CREATE_DOCUMENT) {
			if (resultCode == RESULT_OK && intent != null) {
				intent.data?.let { uri ->
					viewModel.writeDocument(uri)
				}
			} else
				viewModel.resetState() //When no file is created, so we can get back to the correct state
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

	//region events

	private fun observeActions() {
		eventDisposables.clear()
	}

	//endregion
}

