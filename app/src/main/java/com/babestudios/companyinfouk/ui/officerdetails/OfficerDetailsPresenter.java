package com.babestudios.companyinfouk.ui.officerdetails;

import android.support.annotation.NonNull;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

public class OfficerDetailsPresenter extends TiPresenter<OfficerDetailsActivityView> {

	@Inject
	public OfficerDetailsPresenter() {
	}

	@Override
	protected void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onAttachView(@NonNull OfficerDetailsActivityView view) {
		super.onAttachView(view);
		view.showProgress();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
