package com.babestudios.companieshouse.ui.officerappointments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.charges.ChargesItem;
import com.babestudios.companieshouse.data.model.officers.appointments.Appointments;
import com.babestudios.companieshouse.utils.DividerItemDecorationWithSubHeading;
import com.google.gson.Gson;

import net.grandcentrix.thirtyinch.TiActivity;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfficerAppointmentsActivity extends TiActivity<OfficerAppointmentsPresenter, OfficerAppointmentsActivityView> implements OfficerAppointmentsActivityView {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Bind(R.id.officer_appointments_recycler_view)
	RecyclerView officerAppointmentsRecyclerView;

	@Singleton
	@Inject
	DataManager dataManager;

	String officerId;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_officer_appointments);

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

	@NonNull
	@Override
	public OfficerAppointmentsPresenter providePresenter() {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		return new OfficerAppointmentsPresenter(dataManager);
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

	}
}
