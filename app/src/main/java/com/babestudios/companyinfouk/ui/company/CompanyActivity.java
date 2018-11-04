package com.babestudios.companyinfouk.ui.company;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.company.Company;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;
import com.babestudios.companyinfouk.ui.charges.ChargesActivity;
import com.babestudios.companyinfouk.ui.filinghistory.FilingHistoryActivity;
import com.babestudios.companyinfouk.ui.insolvency.InsolvencyActivity;
import com.babestudios.companyinfouk.ui.map.MapActivity;
import com.babestudios.companyinfouk.ui.officers.OfficersActivity;
import com.babestudios.companyinfouk.ui.persons.PersonsActivity;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.babestudios.companyinfouk.utils.DateUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyActivity extends CompositeActivity implements CompanyActivityView {

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.progressbar)
	ProgressBar progressbar;

	@BindView(R.id.tv_incorporated)
	TextView incorporatedTextView;

	@BindView(R.id.tv_company_number)
	TextView companyNumberTextView;

	@BindView(R.id.tv_address_line_1)
	TextView addressLine1TextView;

	@BindView(R.id.tv_address_line_2)
	TextView addressLine2TextView;

	@BindView(R.id.tv_address_postal_code)
	TextView addressPostalCodeTextView;

	@BindView(R.id.tv_address_locality)
	TextView addressLocalityTextView;

	@BindView(R.id.tv_nature_of_business)
	TextView natureOfBusinessTextView;

	@BindView(R.id.tv_accounts)
	TextView accountTextView;

	@BindView(R.id.tv_annual_returns)
	TextView annualReturnsTextView;

	@BindView(R.id.fab)
	FloatingActionButton fab;

	@BindView(R.id.collapsing_toolbar)
	CollapsingToolbarLayout collapsingToolbarLayout;

	String companyNumber;
	String companyName;
	String addressString;

	@Inject
	public CompanyPresenter companyPresenter;

	TiActivityPlugin<CompanyPresenter, CompanyActivityView> companyActivityPlugin = new TiActivityPlugin<>(
			() -> {
				CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
				return companyPresenter;
			});

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public CompanyActivity() {

		addPlugin(companyActivityPlugin);
		addPlugin(baseActivityPlugin);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		companyNumber = getIntent().getStringExtra("companyNumber");
		companyName = getIntent().getStringExtra("companyName");

		companyActivityPlugin.getPresenter().observablesFromViews(RxView.clicks(fab));
		//toolbar_title.setText(companyName);
		companyNumberTextView.setText(companyNumber);
		collapsingToolbarLayout.setTitle(companyName);
	}

	@OnClick(R.id.buttonShowOnMap)
	void onClick() {
		Intent intent = new Intent(this, MapActivity.class);
		intent.putExtra("addressString", addressString);
		intent.putExtra("companyName", companyName);
		baseActivityPlugin.startActivityWithRightSlide(intent);
	}

	@Override
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}

	@Override
	public void onResume() {
		showFab();
		super.onResume();
	}

	@Override
	public void showFab() {
		if (companyActivityPlugin.getPresenter().isFavourite(new SearchHistoryItem(companyName, companyNumber, 0))) {
			fab.setImageResource(R.drawable.favorite_clear_vector);
		} else {
			fab.setImageResource(R.drawable.favorite_vector);
		}
		fab.setScaleX(0f);
		fab.setScaleY(0f);
		fab.setAlpha(0f);
		fab.setVisibility(View.VISIBLE);
		fab.animate().setDuration(getResources().getInteger(R.integer.fab_move_in_duration)).scaleX(1).scaleY(1).alpha(1)
				.setInterpolator(new LinearOutSlowInInterpolator());
	}

	@Override
	public void hideFab() {
		fab.animate().cancel();
		fab.animate().setDuration(getResources().getInteger(R.integer.fab_move_in_duration)).scaleX(0f).scaleY(0f).alpha(0f)
				.setInterpolator(new LinearOutSlowInInterpolator()).withEndAction(this::showFab);

	}

	@Override
	public void showError() {
		Toast.makeText(this, R.string.could_not_retrieve_company_info, Toast.LENGTH_LONG).show();
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
	@SuppressWarnings("ConstantConditions")
	public void showCompany(Company company) {
		getSupportActionBar().setTitle(R.string.company_details);
		incorporatedTextView.setText(String.format(getResources().getString(R.string.incorporated_on), company.dateOfCreation));
		addressLine1TextView.setText(company.registeredOfficeAddress.addressLine1);
		if (company.registeredOfficeAddress.addressLine2 != null) {
			addressLine2TextView.setVisibility(View.VISIBLE);
			addressLine2TextView.setText(company.registeredOfficeAddress.addressLine2);
		}
		addressPostalCodeTextView.setText(company.registeredOfficeAddress.postalCode);
		addressLocalityTextView.setText(company.registeredOfficeAddress.locality);
		String formattedDate;
		if (company.accounts != null && company.accounts.lastAccounts.madeUpTo != null) {
			formattedDate = DateUtil.formatShortDateFromTimeStampMillis(1000 * DateUtil.convertToTimestamp(DateUtil.parseMySqlDate(company.accounts.lastAccounts.madeUpTo)));
			accountTextView.setText(String.format(getResources().getString(R.string.company_accounts_formatted_text), company.accounts.lastAccounts.type, formattedDate));
		} else {
			accountTextView.setText(getResources().getString(R.string.company_accounts_not_found));
		}
		if (company.annualReturn != null && company.annualReturn.lastMadeUpTo != null) {
			formattedDate = DateUtil.formatShortDateFromTimeStampMillis(1000 * DateUtil.convertToTimestamp(DateUtil.parseMySqlDate(company.annualReturn.lastMadeUpTo)));
			annualReturnsTextView.setText(String.format(getResources().getString(R.string.company_annual_returns_formatted_text), formattedDate));
		} else {
			annualReturnsTextView.setText(getResources().getString(R.string.company_annual_returns_not_found));
		}
		addressString = (company.registeredOfficeAddress.addressLine2 != null ? company.registeredOfficeAddress.addressLine2 : company.registeredOfficeAddress.addressLine1) + ", " + company.registeredOfficeAddress.locality + ", " + company.registeredOfficeAddress.postalCode;
	}

	@Override
	public void showNatureOfBusiness(String sicCode, String natureOfBusiness) {
		natureOfBusinessTextView.setText(sicCode + " - " + natureOfBusiness);
	}

	@Override
	public String getCompanyNumber() {
		return companyNumber;
	}

	@Override
	public String getCompanyName() {
		return companyName;
	}

	@Override
	public void showEmptyNatureOfBusiness() {
		natureOfBusinessTextView.setText(getResources().getString(R.string.no_data));
	}

	public void onFilingHistoryClicked(View view) {
		Intent intent = new Intent(this, FilingHistoryActivity.class);
		intent.putExtra("companyNumber", companyNumber);
		baseActivityPlugin.startActivityWithRightSlide(intent);
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	}

	public void onChargesClicked(View view) {
		Intent intent = new Intent(this, ChargesActivity.class);
		intent.putExtra("companyNumber", companyNumber);
		baseActivityPlugin.startActivityWithRightSlide(intent);
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	}

	public void onInsolvencyClicked(View view) {
		Intent intent = new Intent(this, InsolvencyActivity.class);
		intent.putExtra("companyNumber", companyNumber);
		baseActivityPlugin.startActivityWithRightSlide(intent);
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	}

	public void onOfficersClicked(View view) {
		Intent intent = new Intent(this, OfficersActivity.class);
		intent.putExtra("companyNumber", companyNumber);
		baseActivityPlugin.startActivityWithRightSlide(intent);
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	}

	public void onPersonsClicked(View view) {
		Intent intent = new Intent(this, PersonsActivity.class);
		intent.putExtra("companyNumber", companyNumber);
		baseActivityPlugin.startActivityWithRightSlide(intent);
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	}
}
