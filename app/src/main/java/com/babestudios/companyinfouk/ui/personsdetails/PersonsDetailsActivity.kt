package com.babestudios.companyinfouk.ui.personsdetails

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.persons.Person
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.google.gson.Gson
import com.pascalwelsch.compositeandroid.activity.CompositeActivity

class PersonsDetailsActivity : CompositeActivity() {

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
	@BindView(R.id.textViewNotifiedOn)
	internal var textViewNotifiedOn: TextView? = null
	@JvmField
	@BindView(R.id.textViewNaturesOfControl)
	internal var textViewNaturesOfControl: TextView? = null
	@JvmField
	@BindView(R.id.textViewNationality)
	internal var textViewNationality: TextView? = null
	@JvmField
	@BindView(R.id.textViewDateOfBirth)
	internal var textViewDateOfBirth: TextView? = null
	@JvmField
	@BindView(R.id.textViewCountryOfResidence)
	internal var textViewCountryOfResidence: TextView? = null
	@JvmField
	@BindView(R.id.textViewPlaceRegistered)
	internal var textViewPlaceRegistered: TextView? = null
	@JvmField
	@BindView(R.id.textViewRegistrationNumber)
	internal var textViewRegistrationNumber: TextView? = null
	@JvmField
	@BindView(R.id.textViewLabelNationality)
	internal var textViewLabelNationality: TextView? = null
	@JvmField
	@BindView(R.id.textViewLabelDateOfBirth)
	internal var textViewLabelDateOfBirth: TextView? = null
	@JvmField
	@BindView(R.id.textViewLabelCountryOfResidence)
	internal var textViewLabelCountryOfResidence: TextView? = null
	@JvmField
	@BindView(R.id.textViewLabelPlaceRegistered)
	internal var textViewLabelPlaceRegistered: TextView? = null
	@JvmField
	@BindView(R.id.textViewLabelRegistrationNumber)
	internal var textViewLabelRegistrationNumber: TextView? = null
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

	private lateinit var personsItemString: String

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(baseActivityPlugin)
	}

	@SuppressLint("SetTextI18n")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_person_details)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		personsItemString = intent.getStringExtra("personsItem")
		val gson = Gson()
		val person = gson.fromJson(personsItemString, Person::class.java)
		textViewName?.text = person.name
		textViewNotifiedOn?.text = person.notifiedOn
		if (person.nationality == null) {
			textViewNationality?.visibility = View.GONE
			textViewLabelNationality?.visibility = View.GONE
		} else {
			textViewNationality?.visibility = View.VISIBLE
			textViewNationality?.text = person.nationality
		}
		if (person.countryOfResidence == null) {
			textViewCountryOfResidence?.visibility = View.GONE
			textViewLabelCountryOfResidence?.visibility = View.GONE
		} else {
			textViewCountryOfResidence?.visibility = View.VISIBLE
			textViewCountryOfResidence?.text = person.countryOfResidence
		}
		person.dateOfBirth?.let {
			textViewDateOfBirth?.visibility = View.VISIBLE
			textViewDateOfBirth?.text = "${it.month.toString()} / ${it.year.toString()}"
		} ?: run {
			textViewDateOfBirth?.visibility = View.GONE
			textViewLabelDateOfBirth?.visibility = View.GONE
		}
		val naturesOfControl = StringBuilder()
		for (i in person.naturesOfControl.indices) {
			naturesOfControl.append(person.naturesOfControl[i])
			if (i < person.naturesOfControl.size - 1) {
				naturesOfControl.append("; ")
			}
		}
		textViewNaturesOfControl?.text = naturesOfControl.toString()

		textViewAddressLine1?.text = person.address?.addressLine1
		textViewLocality?.text = person.address?.locality
		textViewPostalCode?.text = person.address?.postalCode
		person.identification?.placeRegistered?.let {
			textViewPlaceRegistered?.text = it
			textViewPlaceRegistered?.visibility = View.VISIBLE
		} ?: run {
			textViewPlaceRegistered?.visibility = View.GONE
			textViewLabelPlaceRegistered?.visibility = View.GONE
		}
		person.identification?.registrationNumber?.let {
			textViewRegistrationNumber?.text = it
			textViewRegistrationNumber?.visibility = View.VISIBLE
		} ?: run {
			textViewLabelRegistrationNumber?.visibility = View.GONE
			textViewRegistrationNumber?.visibility = View.GONE
		}
		if (person.address?.region == null) {
			textViewRegion?.visibility = View.GONE
		} else {
			textViewRegion?.visibility = View.VISIBLE
			textViewRegion?.text = person.address?.region
		}
		person.address?.country?.let {
			textViewCountry?.text = it
			textViewCountry?.visibility = View.VISIBLE
		} ?:run {
			textViewCountry?.visibility = View.GONE
		}

		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
			supportActionBar?.setTitle(R.string.person_details)
			toolbar?.setNavigationOnClickListener { onBackPressed() }
		}
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
