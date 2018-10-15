package com.babestudios.companyinfouk.ui.personsdetails;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.persons.Person;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.google.gson.Gson;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonsDetailsActivity extends CompositeActivity {

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.progressbar)
	ProgressBar progressbar;

	@BindView(R.id.textViewName)
	TextView textViewName;
	@BindView(R.id.textViewNotifiedOn)
	TextView textViewNotifiedOn;
	@BindView(R.id.textViewNaturesOfControl)
	TextView textViewNaturesOfControl;
	@BindView(R.id.textViewNationality)
	TextView textViewNationality;
	@BindView(R.id.textViewDateOfBirth)
	TextView textViewDateOfBirth;
	@BindView(R.id.textViewCountryOfResidence)
	TextView textViewCountryOfResidence;
	@BindView(R.id.textViewPlaceRegistered)
	TextView textViewPlaceRegistered;
	@BindView(R.id.textViewRegistrationNumber)
	TextView textViewRegistrationNumber;
	@BindView(R.id.textViewLabelNationality)
	TextView textViewLabelNationality;
	@BindView(R.id.textViewLabelDateOfBirth)
	TextView textViewLabelDateOfBirth;
	@BindView(R.id.textViewLabelCountryOfResidence)
	TextView textViewLabelCountryOfResidence;
	@BindView(R.id.textViewLabelPlaceRegistered)
	TextView textViewLabelPlaceRegistered;
	@BindView(R.id.textViewLabelRegistrationNumber)
	TextView textViewLabelRegistrationNumber;
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

	String personsItemString;

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public PersonsDetailsActivity() {
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_persons_details);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

		ButterKnife.bind(this);
		personsItemString = getIntent().getStringExtra("personsItem");
		Gson gson = new Gson();
		Person person = gson.fromJson(personsItemString, Person.class);
		textViewName.setText(person.name);
		textViewNotifiedOn.setText(person.notifiedOn);
		if (person.nationality == null) {
			textViewNationality.setVisibility(View.GONE);
			textViewLabelNationality.setVisibility(View.GONE);
		} else {
			textViewNationality.setVisibility(View.VISIBLE);
			textViewNationality.setText(person.nationality);
		}
		if (person.countryOfResidence == null) {
			textViewCountryOfResidence.setVisibility(View.GONE);
			textViewLabelCountryOfResidence.setVisibility(View.GONE);
		} else {
			textViewCountryOfResidence.setVisibility(View.VISIBLE);
			textViewCountryOfResidence.setText(person.countryOfResidence);
		}
		if (person.dateOfBirth == null) {
			textViewDateOfBirth.setVisibility(View.GONE);
			textViewLabelDateOfBirth.setVisibility(View.GONE);
		} else {
			textViewDateOfBirth.setVisibility(View.VISIBLE);
			textViewDateOfBirth.setText(person.dateOfBirth.month + "/" + person.dateOfBirth.year);
		}
		StringBuilder naturesOfControl = new StringBuilder();
		for (int i = 0; i < person.naturesOfControl.size(); i++) {
			naturesOfControl.append(person.naturesOfControl.get(i));
			if (i < person.naturesOfControl.size() - 1) {
				naturesOfControl.append("; ");
			}
		}
		textViewNaturesOfControl.setText(naturesOfControl.toString());

		textViewAddressLine1.setText(person.address.addressLine1);
		textViewLocality.setText(person.address.locality);
		textViewPostalCode.setText(person.address.postalCode);
		if (person.identification == null || person.identification.placeRegistered == null) {
			textViewPlaceRegistered.setVisibility(View.GONE);
			textViewLabelPlaceRegistered.setVisibility(View.GONE);
		} else {
			textViewPlaceRegistered.setVisibility(View.VISIBLE);
			textViewPlaceRegistered.setText(person.identification.placeRegistered);
		}
		if (person.identification == null  || person.identification.registrationNumber == null) {
			textViewRegistrationNumber.setVisibility(View.GONE);
			textViewLabelRegistrationNumber.setVisibility(View.GONE);
		} else {
			textViewRegistrationNumber.setVisibility(View.VISIBLE);
			textViewRegistrationNumber.setText(person.identification.registrationNumber);
		}
		if (person.address.region == null) {
			textViewRegion.setVisibility(View.GONE);
		} else {
			textViewRegion.setVisibility(View.VISIBLE);
			textViewRegion.setText(person.address.region);
		}
		if (person.address.country == null) {
			textViewCountry.setVisibility(View.GONE);
		} else {
			textViewCountry.setVisibility(View.VISIBLE);
			textViewCountry.setText(person.address.country);
		}

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.person_details);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
	}

	@Override
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}
}
