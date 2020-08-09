package com.babestudios.companyinfouk.filings.ui.details

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.mvrx.*
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.common.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.filings.databinding.FragmentFilingHistoryDetailsBinding
import com.babestudios.companyinfouk.filings.ui.FilingsViewModel
import com.babestudios.companyinfouk.filings.ui.createSpannableDescription
import io.reactivex.disposables.CompositeDisposable
import java.util.*

private const val REQUEST_WRITE_STORAGE = 1687

class FilingHistoryDetailsFragment : BaseMvRxFragment() {

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

	private fun showFilingHistoryItem() {
		withState(viewModel) { state ->
			val filingHistoryItem = state.filingHistoryItem
			binding.lblFilingHistoryDetailsDate.text = filingHistoryItem.date
			binding.lblFilingHistoryDetailsCategory.text = filingHistoryItem.category.displayName
			binding.lblFilingHistoryDetailsSubcategory.text = filingHistoryItem.subcategory
			binding.lblFilingHistoryDetailsDescription.text = filingHistoryItem.description
			if (filingHistoryItem.description == "legacy" || filingHistoryItem.description == "miscellaneous") {
				binding.lblFilingHistoryDetailsDescription.text = filingHistoryItem.descriptionValues.description
			} else {
				filingHistoryItem.description.let {
					val spannableDescription = filingHistoryItem
							.description
							.createSpannableDescription(filingHistoryItem)
					binding.lblFilingHistoryDetailsDescription.text = spannableDescription
				}
			}

			binding.lblFilingHistoryDetailsPages.text = String.format(Locale.UK, "%d", filingHistoryItem.pages)

			if (filingHistoryItem.category.displayName == "capital"
					&& filingHistoryItem.descriptionValues.capital.isNotEmpty()) {
				filingHistoryItem.descriptionValues.let {
					binding.lblFilingHistoryDetailsDescriptionValues.text = "${it.capital[0].currency} ${it.capital[0].figure}"
				}
			} else {
				binding.lblFilingHistoryDetailsDescriptionValues.visibility = View.GONE
			}
		}
	}

	private fun checkPermissionAndWriteDocument() {
		if (ContextCompat.checkSelfPermission(
						requireContext(),
						Manifest.permission.WRITE_EXTERNAL_STORAGE
				) == PackageManager.PERMISSION_GRANTED) {
			viewModel.writeDocument()
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
				viewModel.writeDocument()
			} else {
				Toast.makeText(
						requireContext(),
						getString(R.string.filing_history_details_wont_save_pdf),
						Toast.LENGTH_LONG
				).show()
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
							checkPermissionAndWriteDocument()
						}
						is Loading -> {
							binding.msvFilingHistoryDetails.viewState = VIEW_STATE_LOADING
						}
						is Success -> {
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


	//endregion
}

