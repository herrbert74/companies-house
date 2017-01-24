package com.babestudios.companyinfouk.ui.officers;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.babestudios.companyinfouk.BuildConfig;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.officers.Officers;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

public class OfficersPresenter extends TiPresenter<OfficersActivityView> implements Observer<Officers> {

	DataManager dataManager;

	@Inject
	public OfficersPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		//CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);

	}

	@Override
	protected void onAttachView(@NonNull OfficersActivityView view) {
		super.onAttachView(view);
		view.showProgress();
		getOfficers();
	}

	@VisibleForTesting
	public void getOfficers() {
		dataManager.getOfficers(getView().getCompanyNumber(), "0").subscribe(this);
	}

	public void loadMoreOfficers(int page) {
		dataManager.getOfficers(getView().getCompanyNumber(), String.valueOf(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE))).subscribe(this);
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

			}
		}
	}

	@Override
	public void onNext(Officers officers) {
		getView().hideProgress();
		getView().showOfficers(officers);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
