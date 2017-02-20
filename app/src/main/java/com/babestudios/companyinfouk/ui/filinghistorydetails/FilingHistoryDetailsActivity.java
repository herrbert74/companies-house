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
import android.text.Spannable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem;
import com.babestudios.companyinfouk.ui.filinghistory.FilingHistoryPresenter;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.google.gson.Gson;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FilingHistoryDetailsActivity extends CompositeActivity implements FilingHistoryDetailsActivityView {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Bind(R.id.textViewDate)
	TextView textViewDate;

	@Bind(R.id.textViewCategory)
	TextView textViewCategory;

	@Bind(R.id.textViewLabelSubcategory)
	TextView textViewLabelSubcategory;

	@Bind(R.id.textViewSubcategory)
	TextView textViewSubcategory;

	@Bind(R.id.textViewDescription)
	TextView textViewDescription;

	@Bind(R.id.textViewDescriptionValues)
	TextView textViewDescriptionValues;

	@Bind(R.id.textViewPages)
	TextView textViewPages;

	@Bind(R.id.textViewLabelPages)
	TextView textViewLabelPages;

	@Inject
	FilingHistoryDetailsPresenter filingHistoryDetailsPresenter;

	String filingHistoryItemString;

	FilingHistoryItem filingHistoryItem;

	private static final int REQUEST_WRITE_STORAGE = 0;

	ResponseBody responseBody;

	TiActivityPlugin<FilingHistoryDetailsPresenter, FilingHistoryDetailsActivityView> filingHistoryDetailsActivityPlugin = new TiActivityPlugin<>(
			() -> {
				CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
				return filingHistoryDetailsPresenter;
			});

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public FilingHistoryDetailsActivity() {
		addPlugin(filingHistoryDetailsActivityPlugin);
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filing_history_details);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

		ButterKnife.bind(this);
		filingHistoryItemString = getIntent().getStringExtra("filingHistoryItem");
		Gson gson = new Gson();
		filingHistoryItem = gson.fromJson(filingHistoryItemString, FilingHistoryItem.class);
		textViewDate.setText(filingHistoryItem.date);
		textViewCategory.setText(filingHistoryItem.category);
		if(filingHistoryItem.subcategory != null) {
			textViewSubcategory.setText(filingHistoryItem.subcategory);
		}else{
			textViewSubcategory.setVisibility(View.GONE);
			textViewLabelSubcategory.setVisibility(View.GONE);
		}
		textViewDescription.setText(filingHistoryItem.description);
		if(filingHistoryItem.description.equals("legacy") || filingHistoryItem.description.equals("miscellaneous")){
			textViewDescription.setText(filingHistoryItem.descriptionValues.description);
		} else {
			Spannable spannableDescription = FilingHistoryPresenter.createSpannableDescription(filingHistoryDetailsPresenter.dataManager.filingHistoryLookup(filingHistoryItem.description), filingHistoryItem);
			textViewDescription.setText(spannableDescription);
		}

		if(filingHistoryItem.pages != null) {
			textViewPages.setText(String.format(Locale.UK, "%d", filingHistoryItem.pages));
		} else {
			textViewLabelPages.setVisibility(View.GONE);
		}

		if (filingHistoryItem.category.equals("capital") && filingHistoryItem.descriptionValues.capital.size() != 0) {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(filingHistoryItem.links != null && filingHistoryItem.links.documentMetadata != null) {
			getMenuInflater().inflate(R.menu.filing_history_details_menu, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_show_pdf:
				filingHistoryDetailsActivityPlugin.getPresenter().getDocument();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void showDocument(Uri uri) {
		Intent target = new Intent(Intent.ACTION_VIEW);
		target.setDataAndType(uri, "application/pdf");
		target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		Intent intent = Intent.createChooser(target, "Open File");
		try {
			baseActivityPlugin.startActivityWithRightSlide(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "Couldn't find PDF reader. Please install one from Google Play.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_WRITE_STORAGE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				filingHistoryDetailsActivityPlugin.getPresenter().writePdf(responseBody);
				Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "The logs won't be saved to the SD card.", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public String getFilingHistoryItemString() {
		return filingHistoryItemString;
	}

	@Override
	public void checkPermissionAndWritePdf(ResponseBody responseBody) {
		if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			filingHistoryDetailsActivityPlugin.getPresenter().writePdf(responseBody);
		} else {
			this.responseBody = responseBody;
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
		}
	}

	@Override
	public void showError() {
		Toast.makeText(this, R.string.could_not_retrieve_filing_history_document_info, Toast.LENGTH_LONG).show();
	}

	@Override
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}
}
