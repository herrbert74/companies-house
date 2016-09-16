package com.babestudios.companieshouse;

import android.app.Application;
import android.content.Context;

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
