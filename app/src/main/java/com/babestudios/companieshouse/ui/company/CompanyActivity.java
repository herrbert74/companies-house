package com.babestudios.companieshouse.ui.company;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.model.company.Company;
import com.babestudios.companieshouse.ui.charges.ChargesActivity;
import com.babestudios.companieshouse.ui.filinghistory.FilingHistoryActivity;
import com.babestudios.companieshouse.ui.insolvency.InsolvencyActivity;
import com.babestudios.companieshouse.ui.officers.OfficersActivity;
import com.babestudios.companieshouse.ui.persons.PersonsActivity;
import com.babestudios.companieshouse.utils.DateUtil;

import net.grandcentrix.thirtyinch.TiActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CompanyActivity extends TiActivity<CompanyPresenter, CompanyActivityView> implements CompanyActivityView {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Bind(R.id.tv_incorporated)
	TextView incorporatedTextView;

	@Bind(R.id.tv_company_number)
	TextView companyNumberTextView;

	@Bind(R.id.tv_address_line_1)
	TextView addressLine1TextView;

	@Bind(R.id.tv_address_line_2)
	TextView addressLine2TextView;

	@Bind(R.id.tv_address_postal_code)
	TextView addressPostalCodeTextView;

	@Bind(R.id.tv_address_locality)
	TextView addressLocalityTextView;

	@Bind(R.id.tv_nature_of_business)
	TextView natureOfBusinessTextView;

	@Bind(R.id.tv_accounts)
	TextView accountTextView;

	@Bind(R.id.tv_annual_returns)
	TextView annualReturnsTextView;

	@Bind(R.id.fab)
	FloatingActionButton fab;

	String companyNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		companyNumber = getIntent().getStringExtra("companyNumber");

		fab.setOnClickListener(view -> getPresenter().onFabClicked());
	}

	@Override
	protected void onResume() {
		super.onResume();
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
	public void showCompany(Company company) {
		getSupportActionBar().setTitle(company.companyName);
		incorporatedTextView.setText(String.format(getResources().getString(R.string.incorporated_on), company.dateOfCreation));
		companyNumberTextView.setText(company.companyNumber);
		addressLine1TextView.setText(company.registeredOfficeAddress.addressLine1);
		if (company.registeredOfficeAddress.addressLine2 != null) {
			addressLine2TextView.setVisibility(View.VISIBLE);
			addressLine2TextView.setText(company.registeredOfficeAddress.addressLine2);
		}
		addressPostalCodeTextView.setText(company.registeredOfficeAddress.postalCode);
		addressLocalityTextView.setText(company.registeredOfficeAddress.locality);
		String formattedDate;
		if(company.accounts.lastAccounts.madeUpTo != null) {
			formattedDate = DateUtil.formatShortDateFromTimeStampMillis(1000 * DateUtil.convertToTimestamp(DateUtil.parseMySqlDate(company.accounts.lastAccounts.madeUpTo)));
			accountTextView.setText(String.format(getResources().getString(R.string.company_accounts_formatted_text), company.accounts.lastAccounts.type, formattedDate));
		} else {
			accountTextView.setText(getResources().getString(R.string.company_accounts_not_found));
		}
		if(company.accounts.lastAccounts.madeUpTo != null) {
			formattedDate = DateUtil.formatShortDateFromTimeStampMillis(1000 * DateUtil.convertToTimestamp(DateUtil.parseMySqlDate(company.annualReturn.lastMadeUpTo)));
			annualReturnsTextView.setText(String.format(getResources().getString(R.string.company_annual_returns_formatted_text), formattedDate));
		}else {
			annualReturnsTextView.setText(getResources().getString(R.string.company_annual_returns_not_found));
		}
	}

	@Override
	public void showNatureOfBusiness(String sicCode, String natureOfBusiness) {
		natureOfBusinessTextView.setText(sicCode + " - " + natureOfBusiness);
	}

	public String getCompanyNumber() {
		return companyNumber;
	}

	@Override
	public void showEmptyNatureOfBusiness() {
		natureOfBusinessTextView.setText(getResources().getString(R.string.no_data));
	}

	@NonNull
	@Override
	public CompanyPresenter providePresenter() {
		return new CompanyPresenter();
	}

	public void onFilingHistoryClicked(View view) {
		Intent intent = new Intent(this, FilingHistoryActivity.class);
		intent.putExtra("companyNumber", companyNumber);
		startActivity(intent);
	}

	public void onChargesClicked(View view) {
		Intent intent = new Intent(this, ChargesActivity.class);
		intent.putExtra("companyNumber", companyNumber);
		startActivity(intent);
	}

	public void onInsolvencyClicked(View view) {
		Intent intent = new Intent(this, InsolvencyActivity.class);
		intent.putExtra("companyNumber", companyNumber);
		startActivity(intent);
	}

	public void onOfficersClicked(View view) {
		Intent intent = new Intent(this, OfficersActivity.class);
		intent.putExtra("companyNumber", companyNumber);
		startActivity(intent);
	}

	public void onPersonsClicked(View view) {
		Intent intent = new Intent(this, PersonsActivity.class);
		intent.putExtra("companyNumber", companyNumber);
		startActivity(intent);
	}
}
