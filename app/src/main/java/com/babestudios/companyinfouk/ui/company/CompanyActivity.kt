package com.babestudios.companyinfouk.ui.company

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.ui.charges.ChargesActivity
import com.babestudios.companyinfouk.ui.filinghistory.FilingHistoryActivity
import com.babestudios.companyinfouk.ui.insolvency.InsolvencyActivity
import com.babestudios.companyinfouk.ui.map.MapActivity
import com.babestudios.companyinfouk.ui.officers.OfficersActivity
import com.babestudios.companyinfouk.ui.persons.PersonsActivity
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.companyinfouk.utils.DateUtil
import com.jakewharton.rxbinding2.view.RxView
import com.pascalwelsch.compositeandroid.activity.CompositeActivity
import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin
import javax.inject.Inject

@Suppress("UNUSED_PARAMETER")
class CompanyActivity : CompositeActivity(), CompanyActivityView {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.progressbar)
	internal var progressbar: ProgressBar? = null

	@JvmField
	@BindView(R.id.tv_incorporated)
	internal var incorporatedTextView: TextView? = null

	@JvmField
	@BindView(R.id.tv_company_number)
	internal var companyNumberTextView: TextView? = null

	@JvmField
	@BindView(R.id.tv_address_line_1)
	internal var addressLine1TextView: TextView? = null

	@JvmField
	@BindView(R.id.tv_address_line_2)
	internal var addressLine2TextView: TextView? = null

	@JvmField
	@BindView(R.id.tv_address_postal_code)
	internal var addressPostalCodeTextView: TextView? = null

	@JvmField
	@BindView(R.id.tv_address_locality)
	internal var addressLocalityTextView: TextView? = null

	@JvmField
	@BindView(R.id.tv_nature_of_business)
	internal var natureOfBusinessTextView: TextView? = null

	@JvmField
	@BindView(R.id.tv_accounts)
	internal var accountTextView: TextView? = null

	@JvmField
	@BindView(R.id.tv_annual_returns)
	internal var annualReturnsTextView: TextView? = null

	@JvmField
	@BindView(R.id.fab)
	internal var fab: FloatingActionButton? = null

	@JvmField
	@BindView(R.id.collapsing_toolbar)
	internal var collapsingToolbarLayout: CollapsingToolbarLayout? = null

	override lateinit var companyNumber: String
	override lateinit var companyName: String
	private lateinit var addressString: String

	@Inject
	lateinit var companyPresenter: CompanyPresenter

	private var companyActivityPlugin = TiActivityPlugin<CompanyPresenter, CompanyActivityView> {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		companyPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {

		addPlugin(companyActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_company)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar?.setDisplayHomeAsUpEnabled(true)

			toolbar?.setNavigationOnClickListener { onBackPressed() }
		}
		companyNumber = intent.getStringExtra("companyNumber")
		companyName = intent.getStringExtra("companyName")

		fab?.let { companyActivityPlugin.presenter.observablesFromViews(RxView.clicks(it)) }
		//toolbar_title.setText(companyName);
		companyNumberTextView?.text = companyNumber
		collapsingToolbarLayout?.title = companyName
	}

	@OnClick(R.id.buttonShowOnMap)
	internal fun onClick() {
		val intent = Intent(this, MapActivity::class.java)
		intent.putExtra("addressString", addressString)
		intent.putExtra("companyName", companyName)
		baseActivityPlugin.startActivityWithRightSlide(intent)
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	override fun onResume() {
		showFab()
		super.onResume()
	}

	override fun showFab() {
		fab?.also {
			if (companyActivityPlugin.presenter.isFavourite(SearchHistoryItem(companyName, companyNumber, 0))) {
				it.setImageResource(R.drawable.ic_favorite_clear)
			} else {
				it.setImageResource(R.drawable.ic_favorite)
			}
			it.scaleX = 0f
			it.scaleY = 0f
			it.alpha = 0f
			it.show()
			it.animate().setDuration(resources.getInteger(R.integer.fab_move_in_duration).toLong()).scaleX(1f).scaleY(1f).alpha(1f).interpolator = LinearOutSlowInInterpolator()
		}
	}

	override fun hideFab() {
		fab?.also {
			it.animate().cancel()
			it.animate().setDuration(resources.getInteger(R.integer.fab_move_in_duration).toLong()).scaleX(0f).scaleY(0f).alpha(0f)
					.setInterpolator(LinearOutSlowInInterpolator()).withEndAction { this.showFab() }
		}

	}

	override fun showError() {
		Toast.makeText(this, R.string.could_not_retrieve_company_info, Toast.LENGTH_LONG).show()
	}

	override fun showProgress() {
		progressbar?.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar?.visibility = View.GONE
	}

	override fun showCompany(company: Company) {
		supportActionBar?.setTitle(R.string.company_details)
		incorporatedTextView?.text = String.format(resources.getString(R.string.incorporated_on), company.dateOfCreation)
		addressLine1TextView?.text = company.registeredOfficeAddress?.addressLine1
		if (company.registeredOfficeAddress?.addressLine2 != null) {
			addressLine2TextView?.visibility = View.VISIBLE
			addressLine2TextView?.text = company.registeredOfficeAddress?.addressLine2
		}
		addressPostalCodeTextView?.text = company.registeredOfficeAddress?.postalCode
		addressLocalityTextView?.text = company.registeredOfficeAddress?.locality
		var formattedDate: String
		company.accounts?.lastAccounts?.madeUpTo?.let {
			val madeUpToDate = DateUtil.parseMySqlDate(it)
			madeUpToDate?.let { date ->
				formattedDate = DateUtil.formatShortDateFromTimeStampMillis(1000 * DateUtil.convertToTimestamp(date))
				accountTextView?.text = String.format(resources.getString(R.string.company_accounts_formatted_text), company.accounts?.lastAccounts?.type, formattedDate)
			}

		} ?: run {
			accountTextView?.text = resources.getString(R.string.company_accounts_not_found)
		}
		company.annualReturn?.lastMadeUpTo?.let {
			val lastMadeUpToDate = DateUtil.parseMySqlDate(it)
			lastMadeUpToDate?.let { date ->
				formattedDate = DateUtil.formatShortDateFromTimeStampMillis(1000 * DateUtil.convertToTimestamp(date))
				annualReturnsTextView?.text = String.format(resources.getString(R.string.company_annual_returns_formatted_text), formattedDate)
			}
		} ?: run {
			annualReturnsTextView?.text = resources.getString(R.string.company_annual_returns_not_found)
		}
		addressString = company.registeredOfficeAddress?.addressLine2?.let {
			it
		} ?: run {
			(company.registeredOfficeAddress?.addressLine1
					+ ", "
					+ company.registeredOfficeAddress?.locality
					+ ", "
					+ company.registeredOfficeAddress?.postalCode)
		}
	}

	@SuppressLint("SetTextI18n")
	override fun showNatureOfBusiness(sicCode: String, natureOfBusiness: String) {
		natureOfBusinessTextView?.text = "$sicCode - $natureOfBusiness"
	}

	override fun showEmptyNatureOfBusiness() {
		natureOfBusinessTextView?.text = resources.getString(R.string.no_data)
	}

	fun onFilingHistoryClicked(view: View) {
		val intent = Intent(this, FilingHistoryActivity::class.java)
		intent.putExtra("companyNumber", companyNumber)
		baseActivityPlugin.startActivityWithRightSlide(intent)
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}

	fun onChargesClicked(view: View) {
		val intent = Intent(this, ChargesActivity::class.java)
		intent.putExtra("companyNumber", companyNumber)
		baseActivityPlugin.startActivityWithRightSlide(intent)
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}

	fun onInsolvencyClicked(view: View) {
		val intent = Intent(this, InsolvencyActivity::class.java)
		intent.putExtra("companyNumber", companyNumber)
		baseActivityPlugin.startActivityWithRightSlide(intent)
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}

	fun onOfficersClicked(view: View) {
		val intent = Intent(this, OfficersActivity::class.java)
		intent.putExtra("companyNumber", companyNumber)
		baseActivityPlugin.startActivityWithRightSlide(intent)
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}

	fun onPersonsClicked(view: View) {
		val intent = Intent(this, PersonsActivity::class.java)
		intent.putExtra("companyNumber", companyNumber)
		baseActivityPlugin.startActivityWithRightSlide(intent)
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}
}
