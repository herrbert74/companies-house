package com.babestudios.companyinfouk.ui.filinghistorydetails

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.ui.filinghistory.FilingHistoryPresenter
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.google.gson.Gson
import com.pascalwelsch.compositeandroid.activity.CompositeActivity

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin

import java.util.Locale

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import okhttp3.ResponseBody

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint

class FilingHistoryDetailsActivity : CompositeActivity(), FilingHistoryDetailsActivityView {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.progressbar)
	internal var progressbar: ProgressBar? = null

	@JvmField
	@BindView(R.id.textViewDate)
	internal var textViewDate: TextView? = null

	@JvmField
	@BindView(R.id.textViewCategory)
	internal var textViewCategory: TextView? = null

	@JvmField
	@BindView(R.id.textViewLabelSubcategory)
	internal var textViewLabelSubcategory: TextView? = null

	@JvmField
	@BindView(R.id.textViewSubcategory)
	internal var textViewSubcategory: TextView? = null

	@JvmField
	@BindView(R.id.textViewDescription)
	internal var textViewDescription: TextView? = null

	@JvmField
	@BindView(R.id.textViewDescriptionValues)
	internal var textViewDescriptionValues: TextView? = null

	@JvmField
	@BindView(R.id.textViewPages)
	internal var textViewPages: TextView? = null

	@JvmField
	@BindView(R.id.textViewLabelPages)
	internal var textViewLabelPages: TextView? = null

	@Inject
	internal lateinit var filingHistoryDetailsPresenter: FilingHistoryDetailsPresenter

	override lateinit var filingHistoryItemString: String

	private lateinit var filingHistoryItem: FilingHistoryItem

	private lateinit var responseBody: ResponseBody

	private var filingHistoryDetailsActivityPlugin = TiActivityPlugin<FilingHistoryDetailsPresenter, FilingHistoryDetailsActivityView> {
		CompaniesHouseApplication.getInstance().applicationComponent.inject(this)
		filingHistoryDetailsPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(filingHistoryDetailsActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	@SuppressLint("SetTextI18n")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_filing_history_details)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		filingHistoryItemString = intent.getStringExtra("filingHistoryItem")
		val gson = Gson()
		filingHistoryItem = gson.fromJson(filingHistoryItemString, FilingHistoryItem::class.java)
		textViewDate?.text = filingHistoryItem.date
		textViewCategory?.text = filingHistoryItem.category
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
				val spannableDescription = FilingHistoryPresenter.createSpannableDescription(filingHistoryDetailsPresenter.dataManager.filingHistoryLookup(it), filingHistoryItem)
				textViewDescription?.text = spannableDescription
			}
		}

		if (filingHistoryItem.pages != null) {
			textViewPages?.text = String.format(Locale.UK, "%d", filingHistoryItem.pages)
		} else {
			textViewLabelPages?.visibility = View.GONE
		}

		if (filingHistoryItem.category == "capital" && filingHistoryItem.descriptionValues?.capital?.isNotEmpty() == true) {
			filingHistoryItem.descriptionValues?.let {
				textViewDescriptionValues?.text = "${it.capital[0].currency} ${it.capital[0].figure}"
			}
		} else {
			textViewDescriptionValues?.visibility = View.GONE
		}

		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
			supportActionBar?.setTitle(R.string.filing_history_details)
			toolbar?.setNavigationOnClickListener { onBackPressed() }
		}

	}

	override fun showProgress() {
		progressbar?.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar?.visibility = View.GONE
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		filingHistoryItem.links?.documentMetadata?.let {
			menuInflater.inflate(R.menu.filing_history_details_menu, menu)
		}
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.action_show_pdf -> {
				filingHistoryDetailsActivityPlugin.presenter.getDocument(filingHistoryItemString)
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun showDocument(pdfBytes: Uri) {
		val target = Intent(Intent.ACTION_VIEW)
		target.setDataAndType(pdfBytes, "application/pdf")
		target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
		val intent = Intent.createChooser(target, "Open File")
		try {
			baseActivityPlugin.startActivityWithRightSlide(intent)
		} catch (e: ActivityNotFoundException) {
			Toast.makeText(this, "Couldn't find PDF reader. Please install one from Google Play.", Toast.LENGTH_LONG).show()
		}

	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		if (requestCode == REQUEST_WRITE_STORAGE) {
			if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				filingHistoryDetailsActivityPlugin.presenter.writePdf(responseBody)
			} else {
				Toast.makeText(this, "The logs won't be saved to the SD card.", Toast.LENGTH_LONG).show()
			}
		}
	}

	override fun checkPermissionAndWritePdf(responseBody: ResponseBody) {
		if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			filingHistoryDetailsActivityPlugin.presenter.writePdf(responseBody)
		} else {
			this.responseBody = responseBody
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_STORAGE)
		}
	}

	override fun showError() {
		Toast.makeText(this, R.string.could_not_retrieve_filing_history_document_info, Toast.LENGTH_LONG).show()
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	companion object {

		private const val REQUEST_WRITE_STORAGE = 0
	}
}
