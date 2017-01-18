package com.babestudios.companieshouse.ui.officerdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.officers.OfficerItem;
import com.babestudios.companieshouse.ui.officerappointments.OfficerAppointmentsActivity;
import com.google.gson.Gson;

import net.grandcentrix.thirtyinch.TiActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfficerDetailsActivity extends TiActivity<OfficerDetailsPresenter, OfficerDetailsActivityView> implements OfficerDetailsActivityView {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Bind(R.id.textViewName)
	TextView textViewName;
	@Bind(R.id.textViewAppointedOn)
	TextView textViewAppointedOn;
	@Bind(R.id.textViewNationality)
	TextView textViewNationality;
	@Bind(R.id.textViewOccupation)
	TextView textViewOccupation;
	@Bind(R.id.textViewDateOfBirth)
	TextView textViewDateOfBirth;
	@Bind(R.id.textViewCountryOfResidence)
	TextView textViewCountryOfResidence;
	@Bind(R.id.textViewAddressLine1)
	TextView textViewAddressLine1;
	@Bind(R.id.textViewLocality)
	TextView textViewLocality;
	@Bind(R.id.textViewPostalCode)
	TextView textViewPostalCode;
	@Bind(R.id.textViewRegion)
	TextView textViewRegion;
	@Bind(R.id.textViewCountry)
	TextView textViewCountry;

	String officerItemString;
	String officerId;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_officer_details);

		ButterKnife.bind(this);
		officerItemString = getIntent().getStringExtra("officerItem");
		Gson gson = new Gson();
		OfficerItem officerItem = gson.fromJson(officerItemString, OfficerItem.class);
		textViewName.setText(officerItem.name);
		textViewAppointedOn.setText(officerItem.appointedOn);
		textViewNationality.setText(officerItem.nationality);
		textViewOccupation.setText(officerItem.occupation);
		if(officerItem.dateOfBirth != null) {
			textViewDateOfBirth.setText(officerItem.dateOfBirth.month + "/" + officerItem.dateOfBirth.year);
		} else {
			textViewDateOfBirth.setText(R.string.officer_details_unknown);
		}
		textViewCountryOfResidence.setText(officerItem.countryOfResidence);
		textViewAddressLine1.setText(officerItem.address.addressLine1);
		textViewLocality.setText(officerItem.address.locality);
		textViewPostalCode.setText(officerItem.address.postalCode);
		if(officerItem.address.region == null) {
			textViewRegion.setVisibility(View.GONE);
		} else {
			textViewRegion.setVisibility(View.VISIBLE);
			textViewRegion.setText(officerItem.address.region);
		}
		if(officerItem.address.country == null) {
			textViewCountry.setVisibility(View.GONE);
		} else {
			textViewCountry.setVisibility(View.VISIBLE);
			textViewCountry.setText(officerItem.address.country);
		}

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.officer_details);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		Pattern pattern = Pattern.compile("officers/(.+)/appointments");
		Matcher matcher = pattern.matcher(officerItem.links.officer.appointments);
		if (matcher.find())
		{
			officerId = matcher.group(1);
		}
	}

	@OnClick(R.id.buttonAppointments)
	public void onClick() {
		Intent intent = new Intent(this, OfficerAppointmentsActivity.class);
		intent.putExtra("officerId", officerId);
		startActivity(intent);
	}

	@Override
	public void showProgress() {
		progressbar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progressbar.setVisibility(View.GONE);
	}

	@NonNull
	@Override
	public OfficerDetailsPresenter providePresenter() {
		return new OfficerDetailsPresenter();
	}
}
