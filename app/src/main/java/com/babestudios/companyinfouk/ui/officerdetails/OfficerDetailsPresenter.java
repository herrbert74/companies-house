package com.babestudios.companyinfouk.ui.officerdetails;

import android.support.annotation.NonNull;

import net.grandcentrix.thirtyinch.TiPresenter;

public class OfficerDetailsPresenter extends TiPresenter<OfficerDetailsActivityView> {

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
