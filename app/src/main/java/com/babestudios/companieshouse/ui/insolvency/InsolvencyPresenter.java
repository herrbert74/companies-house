package com.babestudios.companieshouse.ui.insolvency;

import android.util.Log;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.insolvency.Insolvency;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

public class InsolvencyPresenter extends TiPresenter<InsolvencyActivityView> implements Observer<Insolvency> {

	DataManager dataManager;

	@Inject
	public InsolvencyPresenter(DataManager dataManager) {
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
		getInsolvency();
	}

	private void getInsolvency() {
		dataManager.getInsolvency(getView().getCompanyNumber()).subscribe(this);
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		getView().hideProgress();
		Log.d("test", "onError: " + e.fillInStackTrace());
		if(e instanceof HttpException){
			HttpException h = (HttpException) e;
			if(h.code() == 404){
				getView().showNoInsolvency();
			}
		}
	}

	@Override
	public void onNext(Insolvency insolvency) {
		getView().hideProgress();
		if(insolvency != null) {
			getView().showInsolvency(insolvency);
		} else {
			getView().showNoInsolvency();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
