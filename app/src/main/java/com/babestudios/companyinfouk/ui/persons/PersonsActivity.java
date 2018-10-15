package com.babestudios.companyinfouk.ui.persons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.persons.Person;
import com.babestudios.companyinfouk.data.model.persons.Persons;
import com.babestudios.companyinfouk.ui.personsdetails.PersonsDetailsActivity;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.babestudios.companyinfouk.utils.DividerItemDecoration;
import com.google.gson.Gson;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonsActivity extends CompositeActivity implements PersonsActivityView, PersonsAdapter.PersonsRecyclerViewClickListener {

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.persons_recycler_view)
	RecyclerView personsRecyclerView;

	@BindView(R.id.lblNoPersons)
	TextView lblNoPersons;

	private PersonsAdapter personsAdapter;

	@BindView(R.id.progressbar)
	ProgressBar progressbar;

	@Inject
	PersonsPresenter personsPresenter;

	String companyNumber;

	TiActivityPlugin<PersonsPresenter, PersonsActivityView> personsActivityPlugin = new TiActivityPlugin<>(
			() -> {
				CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
				return personsPresenter;
			});

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public PersonsActivity() {
		addPlugin(personsActivityPlugin);
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_persons);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.persons_with_significant_control);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		companyNumber = getIntent().getStringExtra("companyNumber");
		createPersonsRecyclerView();


	}
	private void createPersonsRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		personsRecyclerView.setLayoutManager(linearLayoutManager);
		personsRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));
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
	public void showPersons(Persons persons) {
		if (personsRecyclerView.getAdapter() == null) {
			personsAdapter = new PersonsAdapter(PersonsActivity.this, persons);
			personsRecyclerView.setAdapter(personsAdapter);
		} else {
			personsAdapter.updateItems(persons);
		}
	}

	@Override
	public void showNoPersons() {
		lblNoPersons.setVisibility(View.VISIBLE);
		personsRecyclerView.setVisibility(View.GONE);
	}

	@Override
	public void personsItemClicked(View v, int position, Person person) {
		Gson gson = new Gson();
		String jsonItem = gson.toJson(person, Person.class);
		Intent intent = new Intent(this, PersonsDetailsActivity.class);
		intent.putExtra("personsItem", jsonItem);
		baseActivityPlugin.startActivityWithRightSlide(intent);
	}

	@Override
	public String getCompanyNumber() {
		return companyNumber;
	}

	@Override
	public void showError() {
		Toast.makeText(this, R.string.could_not_retrieve_persons_info, Toast.LENGTH_LONG).show();
	}

	@Override
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}
}
