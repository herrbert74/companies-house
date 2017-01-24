package com.babestudios.companyinfouk.ui.insolvency;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency;

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
	}

	@Override
	protected void onAttachView(@NonNull InsolvencyActivityView view) {
		super.onAttachView(view);
		view.showProgress();
		getInsolvency();
	}

	@VisibleForTesting
	public void getInsolvency() {
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
