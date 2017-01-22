package com.babestudios.companyinfouk;

import android.app.Application;
import android.content.Context;

import com.babestudios.companyinfouk.injection.ApplicationComponent;
import com.babestudios.companyinfouk.injection.ApplicationModule;
import com.babestudios.companyinfouk.injection.DaggerApplicationComponent;

import net.grandcentrix.thirtyinch.TiConfiguration;
import net.grandcentrix.thirtyinch.TiPresenter;

public class CompaniesHouseApplication extends Application {

	private static CompaniesHouseApplication instance;

	public CompaniesHouseApplication() {
		instance = this;
	}

	ApplicationComponent mApplicationComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplicationComponent = DaggerApplicationComponent.builder()
				.applicationModule(new ApplicationModule(this))
				.build();
		mApplicationComponent.inject(this);
		TiPresenter.setDefaultConfig(PRESENTER_CONFIG);
	}

	public static Context getContext() {
		return instance;
	}

	public ApplicationComponent getApplicationComponent() {
		return mApplicationComponent;
	}

	public static CompaniesHouseApplication getInstance() {
		return instance;
	}

	public static final TiConfiguration PRESENTER_CONFIG =
			new TiConfiguration.Builder()
					.setRetainPresenterEnabled(false)
					.build();


}
