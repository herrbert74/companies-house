package com.babestudios.companyinfouk.ui.personsdetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.persons.Person;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PersonsDetailsActivity extends AppCompatActivity {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Bind(R.id.textViewName)
	TextView textViewName;
	@Bind(R.id.textViewNotifiedOn)
	TextView textViewNotifiedOn;
	@Bind(R.id.textViewNaturesOfControl)
	TextView textViewNaturesOfControl;
	@Bind(R.id.textViewNationality)
	TextView textViewNationality;
	@Bind(R.id.textViewDateOfBirth)
	TextView textViewDateOfBirth;
	@Bind(R.id.textViewCountryOfResidence)
	TextView textViewCountryOfResidence;
	@Bind(R.id.textViewPlaceRegistered)
	TextView textViewPlaceRegistered;
	@Bind(R.id.textViewRegistrationNumber)
	TextView textViewRegistrationNumber;
	@Bind(R.id.textViewLabelNationality)
	TextView textViewLabelNationality;
	@Bind(R.id.textViewLabelDateOfBirth)
	TextView textViewLabelDateOfBirth;
	@Bind(R.id.textViewLabelCountryOfResidence)
	TextView textViewLabelCountryOfResidence;
	@Bind(R.id.textViewLabelPlaceRegistered)
	TextView textViewLabelPlaceRegistered;
	@Bind(R.id.textViewLabelRegistrationNumber)
	TextView textViewLabelRegistrationNumber;
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

	String personsItemString;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_persons_details);

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
}
