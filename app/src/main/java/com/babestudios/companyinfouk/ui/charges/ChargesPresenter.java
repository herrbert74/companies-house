package com.babestudios.companyinfouk.ui.charges;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.babestudios.companyinfouk.BuildConfig;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.charges.Charges;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public class ChargesPresenter extends TiPresenter<ChargesActivityView> implements Observer<Charges> {

	DataManager dataManager;

	@Inject
	public ChargesPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onAttachView(@NonNull ChargesActivityView view) {
		super.onAttachView(view);
		view.showProgress();
		getCharges();
	}

	@VisibleForTesting
	public void getCharges() {
		dataManager.getCharges(getView().getCompanyNumber(), "0").subscribe(this);
	}

	public void loadMoreCharges(int page) {
		dataManager.getCharges(getView().getCompanyNumber(), String.valueOf(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE))).subscribe(this);
	}

	@Override
	public void onComplete() {
	}

	@Override
	public void onSubscribe(Disposable d) {

	}

	@Override
	public void onError(Throwable e) {
		getView().hideProgress();
		Log.d("test", "onError: " + e.fillInStackTrace());
		if (e instanceof HttpException) {
			HttpException h = (HttpException) e;
			if (h.code() == 404) {
				getView().showNoCharges();
			} else {
				getView().showError();
			}
		} else {
			getView().showError();
		}
	}

	@Override
	public void onNext(Charges charges) {
		getView().hideProgress();
		getView().showCharges(charges);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
