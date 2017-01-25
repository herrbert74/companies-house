package com.babestudios.companyinfouk.ui.filinghistorydetails;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem;
import com.google.gson.Gson;

import net.grandcentrix.thirtyinch.TiActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FilingHistoryDetailsActivity extends TiActivity<FilingHistoryDetailsPresenter, FilingHistoryDetailsActivityView> implements FilingHistoryDetailsActivityView {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Bind(R.id.textViewDate)
	TextView textViewDate;

	@Bind(R.id.textViewCategory)
	TextView textViewCategory;

	@Bind(R.id.textViewDescription)
	TextView textViewDescription;

	@Bind(R.id.textViewDescriptionValues)
	TextView textViewDescriptionValues;

	@Bind(R.id.textViewPages)
	TextView textViewPages;

	@Singleton
	@Inject
	DataManager dataManager;

	String filingHistoryItemString;

	private static final int REQUEST_WRITE_STORAGE = 0;

	ResponseBody responseBody;

	@Override
	public String getFilingHistoryItemString() {
		return filingHistoryItemString;
	}

	@Override
	public void checkPermissionAndWritePdf(ResponseBody responseBody) {
		if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			getPresenter().writePdf(responseBody);
		} else {
			this.responseBody = responseBody;
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filing_history_details);

		ButterKnife.bind(this);
		filingHistoryItemString = getIntent().getStringExtra("filingHistoryItem");
		Gson gson = new Gson();
		FilingHistoryItem filingHistoryItem = gson.fromJson(filingHistoryItemString, FilingHistoryItem.class);
		textViewDate.setText(filingHistoryItem.date);
		textViewCategory.setText(filingHistoryItem.category);
		textViewDescription.setText(filingHistoryItem.description);
		textViewPages.setText(Integer.toString(filingHistoryItem.pages));

		if (filingHistoryItem.category.equals("capital")) {
			textViewDescriptionValues.setText(filingHistoryItem.descriptionValues.capital.get(0).currency + " " + filingHistoryItem.descriptionValues.capital.get(0).figure);
		} else {
			textViewDescriptionValues.setVisibility(View.GONE);
		}

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.filing_history_details);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}

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
	public FilingHistoryDetailsPresenter providePresenter() {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		return new FilingHistoryDetailsPresenter(dataManager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.filing_history_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_show_pdf:
				getPresenter().getDocument();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void showDocument(Uri uri) {
		Intent target = new Intent(Intent.ACTION_VIEW);
		target.setDataAndType(uri,"application/pdf");
		target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		Intent intent = Intent.createChooser(target, "Open File");
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "Couldn't find PDF reader. Please install one from Google Play.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_WRITE_STORAGE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				getPresenter().writePdf(responseBody);
				Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "The logs won't be saved to the SD card.", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void showError() {
		Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
	}
}
