package com.babestudios.companyinfouk.ui.officerdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.officers.OfficerItem;
import com.babestudios.companyinfouk.ui.officerappointments.OfficerAppointmentsActivity;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.google.gson.Gson;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfficerDetailsActivity extends CompositeActivity implements OfficerDetailsActivityView {

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.progressbar)
	ProgressBar progressbar;

	@BindView(R.id.textViewName)
	TextView textViewName;
	@BindView(R.id.textViewAppointedOn)
	TextView textViewAppointedOn;
	@BindView(R.id.textViewNationality)
	TextView textViewNationality;
	@BindView(R.id.textViewOccupation)
	TextView textViewOccupation;
	@BindView(R.id.textViewDateOfBirth)
	TextView textViewDateOfBirth;
	@BindView(R.id.textViewCountryOfResidence)
	TextView textViewCountryOfResidence;
	@BindView(R.id.textViewAddressLine1)
	TextView textViewAddressLine1;
	@BindView(R.id.textViewLocality)
	TextView textViewLocality;
	@BindView(R.id.textViewPostalCode)
	TextView textViewPostalCode;
	@BindView(R.id.textViewRegion)
	TextView textViewRegion;
	@BindView(R.id.textViewCountry)
	TextView textViewCountry;

	String officerItemString;
	String officerId;

	@Inject
	public OfficerDetailsPresenter officerDetailsPresenter;

	TiActivityPlugin<OfficerDetailsPresenter, OfficerDetailsActivityView> officerDetailsActivityPlugin = new TiActivityPlugin<>(
			() -> {
				CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
				return officerDetailsPresenter;
			});

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public OfficerDetailsActivity() {
		addPlugin(officerDetailsActivityPlugin);
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_officer_details);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

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
		baseActivityPlugin.startActivityWithRightSlide(intent);
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
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}
}
