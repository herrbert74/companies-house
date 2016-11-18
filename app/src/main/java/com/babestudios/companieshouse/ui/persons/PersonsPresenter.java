package com.babestudios.companieshouse.ui.persons;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.babestudios.companieshouse.BuildConfig;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.persons.Persons;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

public class PersonsPresenter extends TiPresenter<PersonsActivityView> implements Observer<Persons> {

	DataManager dataManager;

	@Inject
	public PersonsPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		//CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);

	}

	@Override
	protected void onWakeUp() {
		super.onWakeUp();
		getView().showProgress();
		getPersons();
	}

	@VisibleForTesting
	public void getPersons() {
		dataManager.getPersons(getView().getCompanyNumber(), "0").subscribe(this);
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		getView().hideProgress();
		Log.d("test", "onError: " + e.fillInStackTrace());
		if (e instanceof HttpException) {
			HttpException h = (HttpException) e;
			Log.d("test", "onError: " + h.code());
			if (h.code() == 404) {
				getView().showNoPersons();
			}
		}
	}

	@Override
	public void onNext(Persons persons) {
		getView().hideProgress();
		getView().showPersons(persons);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
