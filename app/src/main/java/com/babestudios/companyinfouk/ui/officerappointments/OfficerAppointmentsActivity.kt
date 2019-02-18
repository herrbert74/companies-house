package com.babestudios.companyinfouk.ui.officerappointments

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointment
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.pascalwelsch.compositeandroid.activity.CompositeActivity

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin

import java.util.ArrayList

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.base.ext.biLet
import com.babestudios.companyinfouk.ui.company.createCompanyIntent

class OfficerAppointmentsActivity : CompositeActivity(), OfficerAppointmentsActivityView, OfficerAppointmentsAdapter.AppointmentsRecyclerViewClickListener {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.progressbar)
	internal var progressbar: ProgressBar? = null

	@JvmField
	@BindView(R.id.officer_appointments_recycler_view)
	internal var officerAppointmentsRecyclerView: RecyclerView? = null

	@Inject
	internal lateinit var officerAppointmentsPresenter: OfficerAppointmentsPresenter

	override lateinit var officerId: String

	private var officerAppointmentsActivityPlugin = TiActivityPlugin<OfficerAppointmentsPresenter, OfficerAppointmentsActivityView> {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		officerAppointmentsPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(officerAppointmentsActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_officer_appointments)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)

		officerId = intent.getStringExtra("officerId")

		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
			supportActionBar?.setTitle(R.string.officer_appointments_title)
			toolbar?.setNavigationOnClickListener { onBackPressed() }
		}

	}

	private fun createRecyclerView(appointments: Appointments) {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		officerAppointmentsRecyclerView?.layoutManager = linearLayoutManager
		val titlePositions = ArrayList<Int>()
		titlePositions.add(0)
		officerAppointmentsRecyclerView?.addItemDecoration(
				DividerItemDecorationWithSubHeading(this, titlePositions))
		val adapter = OfficerAppointmentsAdapter(this, appointments)
		officerAppointmentsRecyclerView?.adapter = adapter
	}

	override fun showProgress() {
		progressbar?.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar?.visibility = View.GONE
	}

	override fun showAppointments(appointments: Appointments) {
		createRecyclerView(appointments)
	}

	override fun showError() {
		Toast.makeText(this, R.string.could_not_retrieve_officer_appointments_info, Toast.LENGTH_LONG).show()
	}

	override fun appointmentItemClicked(v: View, position: Int, item: Appointment) {
		(item.appointedTo?.companyNumber to item.appointedTo?.companyName).biLet { companyNumber, companyName ->
			startCompanyActivity(companyNumber, companyName)
		}
	}

	override fun startCompanyActivity(companyNumber: String, companyName: String) {
		baseActivityPlugin.startActivityWithRightSlide(createCompanyIntent(companyNumber, companyName))
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
