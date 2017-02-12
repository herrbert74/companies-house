package com.babestudios.companyinfouk.ui.officerappointments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointment;
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments;
import com.babestudios.companyinfouk.ui.company.CompanyActivity;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.babestudios.companyinfouk.utils.DividerItemDecorationWithSubHeading;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfficerAppointmentsActivity extends CompositeActivity implements OfficerAppointmentsActivityView,
		OfficerAppointmentsAdapter.AppointmentsRecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Bind(R.id.officer_appointments_recycler_view)
	RecyclerView officerAppointmentsRecyclerView;

	@Inject
	OfficerAppointmentsPresenter officerAppointmentsPresenter;

	String officerId;

	TiActivityPlugin<OfficerAppointmentsPresenter, OfficerAppointmentsActivityView> officerAppointmentsActivityPlugin = new TiActivityPlugin<>(
			() -> {
				CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
				return officerAppointmentsPresenter;
			});

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public OfficerAppointmentsActivity() {
		addPlugin(officerAppointmentsActivityPlugin);
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_officer_appointments);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

		ButterKnife.bind(this);

		officerId = getIntent().getStringExtra("officerId");

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.officer_appointments_title);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}

	}

	private void createChargesDetailsRecyclerView(Appointments appointments) {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		officerAppointmentsRecyclerView.setLayoutManager(linearLayoutManager);
		ArrayList<Integer> titlePositions = new ArrayList<>();
		titlePositions.add(0);
		officerAppointmentsRecyclerView.addItemDecoration(
				new DividerItemDecorationWithSubHeading(this, titlePositions));
		OfficerAppointmentsAdapter adapter = new OfficerAppointmentsAdapter(this, appointments);
		officerAppointmentsRecyclerView.setAdapter(adapter);
	}

	@Override
	public void showProgress() {
		progressbar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progressbar.setVisibility(View.GONE);
	}

	@Override
	public void showAppointments(Appointments appointments) {
		createChargesDetailsRecyclerView(appointments);
	}

	@Override
	public String getOfficerId() {
		return officerId;
	}

	@Override
	public void showError() {
		Toast.makeText(this, R.string.could_not_retrieve_officer_appointments_info, Toast.LENGTH_LONG).show();
	}

	@Override
	public void appointmentItemClicked(View v, int position, Appointment item) {
		startCompanyActivity(item.appointedTo.companyNumber, item.appointedTo.companyName);
	}

	@Override
	public void startCompanyActivity(String companyNumber, String companyName) {
		Intent startIntent = new Intent(this, CompanyActivity.class);
		startIntent.putExtra("companyNumber", companyNumber);
		startIntent.putExtra("companyName", companyName);
		baseActivityPlugin.startActivityWithRightSlide(startIntent);
	}

	@Override
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}
}
