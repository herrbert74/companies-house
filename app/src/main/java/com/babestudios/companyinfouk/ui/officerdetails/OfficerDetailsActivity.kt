package com.babestudios.companyinfouk.ui.officerdetails

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import com.babestudios.companyinfouk.ui.officerappointments.OfficerAppointmentsActivity
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.google.gson.Gson
import com.pascalwelsch.compositeandroid.activity.CompositeActivity

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin

import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

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

	internal var officerDetailsActivityPlugin = TiActivityPlugin<OfficerDetailsPresenter, OfficerDetailsActivityView> {
		CompaniesHouseApplication.getInstance().applicationComponent.inject(this)
		officerDetailsPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(officerDetailsActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_officer_details)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		officerItemString = intent.getStringExtra("officerItem")
		val gson = Gson()
		val officerItem = gson.fromJson(officerItemString, OfficerItem::class.java)
		textViewName!!.text = officerItem.name
		textViewAppointedOn!!.text = officerItem.appointedOn
		textViewNationality!!.text = officerItem.nationality
		textViewOccupation!!.text = officerItem.occupation
		if (officerItem.dateOfBirth != null) {
			textViewDateOfBirth!!.text = officerItem.dateOfBirth.month.toString() + "/" + officerItem.dateOfBirth.year
		} else {
			textViewDateOfBirth!!.setText(R.string.officer_details_unknown)
		}
		textViewCountryOfResidence!!.text = officerItem.countryOfResidence
		textViewAddressLine1!!.text = officerItem.address.addressLine1
		textViewLocality!!.text = officerItem.address.locality
		textViewPostalCode!!.text = officerItem.address.postalCode
		if (officerItem.address.region == null) {
			textViewRegion!!.visibility = View.GONE
		} else {
			textViewRegion!!.visibility = View.VISIBLE
			textViewRegion!!.text = officerItem.address.region
		}
		if (officerItem.address.country == null) {
			textViewCountry!!.visibility = View.GONE
		} else {
			textViewCountry!!.visibility = View.VISIBLE
			textViewCountry!!.text = officerItem.address.country
		}

		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar!!.setDisplayHomeAsUpEnabled(true)
			supportActionBar!!.setTitle(R.string.officer_details)
			toolbar!!.setNavigationOnClickListener { v -> onBackPressed() }
		}
		val pattern = Pattern.compile("officers/(.+)/appointments")
		val matcher = pattern.matcher(officerItem.links.officer.appointments)
		if (matcher.find()) {
			officerId = matcher.group(1)
		}
	}

	@OnClick(R.id.buttonAppointments)
	fun onClick() {
		val intent = Intent(this, OfficerAppointmentsActivity::class.java)
		intent.putExtra("officerId", officerId)
		baseActivityPlugin.startActivityWithRightSlide(intent)
	}

	override fun showProgress() {
		progressbar!!.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar!!.visibility = View.GONE
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
