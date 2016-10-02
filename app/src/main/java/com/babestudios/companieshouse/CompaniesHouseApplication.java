package com.babestudios.companieshouse;

import android.app.Application;
import android.content.Context;

import com.babestudios.companieshouse.injection.ApplicationComponent;
import com.babestudios.companieshouse.injection.ApplicationModule;
import com.babestudios.companieshouse.injection.DaggerApplicationComponent;

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
}
