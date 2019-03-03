package com.babestudios.companyinfouk.ui.officerdetails

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import com.babestudios.companyinfouk.ui.officerappointments.createOfficerAppointmentsIntent
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.google.gson.Gson
import com.pascalwelsch.compositeandroid.activity.CompositeActivity
import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin
import java.util.regex.Pattern
import javax.inject.Inject

class OfficerDetailsActivity : CompositeActivity(), OfficerDetailsActivityView {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.progressbar)
	internal var progressbar: ProgressBar? = null

	@JvmField
	@BindView(R.id.textViewName)
	internal var textViewName: TextView? = null
	@JvmField
	@BindView(R.id.textViewAppointedOn)
	internal var textViewAppointedOn: TextView? = null
	@JvmField
	@BindView(R.id.textViewNationality)
	internal var textViewNationality: TextView? = null
	@JvmField
	@BindView(R.id.textViewOccupation)
	internal var textViewOccupation: TextView? = null
	@JvmField
	@BindView(R.id.textViewDateOfBirth)
	internal var textViewDateOfBirth: TextView? = null
	@JvmField
	@BindView(R.id.textViewCountryOfResidence)
	internal var textViewCountryOfResidence: TextView? = null
	@JvmField
	@BindView(R.id.textViewAddressLine1)
	internal var textViewAddressLine1: TextView? = null
	@JvmField
	@BindView(R.id.textViewLocality)
	internal var textViewLocality: TextView? = null
	@JvmField
	@BindView(R.id.textViewPostalCode)
	internal var textViewPostalCode: TextView? = null
	@JvmField
	@BindView(R.id.textViewRegion)
	internal var textViewRegion: TextView? = null
	@JvmField
	@BindView(R.id.textViewCountry)
	internal var textViewCountry: TextView? = null

	private lateinit var officerItemString: String
	private lateinit var officerId: String

	@Inject
	lateinit var officerDetailsPresenter: OfficerDetailsPresenter

	private var officerDetailsActivityPlugin = TiActivityPlugin<OfficerDetailsPresenter, OfficerDetailsActivityView> {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		officerDetailsPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(officerDetailsActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	@SuppressLint("SetTextI18n")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_officer_details)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		officerItemString = intent.getStringExtra("officerItem")
		val gson = Gson()
		val officerItem = gson.fromJson(officerItemString, OfficerItem::class.java)
		textViewName?.text = officerItem.name
		textViewAppointedOn?.text = officerItem.appointedOn
		textViewNationality?.text = officerItem.nationality
		textViewOccupation?.text = officerItem.occupation
		officerItem.dateOfBirth?.let {
			textViewDateOfBirth?.text = "${it.month.toString()} / ${it.year.toString()}"
		} ?: run {
			textViewDateOfBirth?.setText(R.string.officer_details_unknown)
		}
		textViewCountryOfResidence?.text = officerItem.countryOfResidence
		textViewAddressLine1?.text = officerItem.address?.addressLine1
		textViewLocality?.text = officerItem.address?.locality
		textViewPostalCode?.text = officerItem.address?.postalCode
		officerItem.address?.region?.let {
			textViewRegion?.visibility = View.VISIBLE
			textViewRegion?.text = it
		} ?: run {
			textViewRegion?.visibility = View.GONE
		}
		officerItem.address?.country?.let {
			textViewCountry?.text = it
			textViewCountry?.visibility = View.VISIBLE
		} ?: run {
			textViewCountry?.visibility = View.GONE
		}

		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
			supportActionBar?.setTitle(R.string.officer_details)
			toolbar?.setNavigationOnClickListener { onBackPressed() }
		}
		val pattern = Pattern.compile("officers/(.+)/appointments")
		val matcher = pattern.matcher(officerItem.links?.officer?.appointments)
		if (matcher.find()) {
			officerId = matcher.group(1)
		}
	}

	@OnClick(R.id.buttonAppointments)
	fun onClick() {
		baseActivityPlugin.startActivityWithRightSlide(createOfficerAppointmentsIntent(officerId))
	}

	override fun showProgress() {
		progressbar?.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar?.visibility = View.GONE
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
