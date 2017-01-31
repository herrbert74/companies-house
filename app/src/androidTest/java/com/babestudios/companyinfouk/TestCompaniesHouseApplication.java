package com.babestudios.companyinfouk;

import android.content.Context;

public class TestCompaniesHouseApplication extends CompaniesHouseApplication {

	private static TestCompaniesHouseApplication instance;

	public TestCompaniesHouseApplication() {
		instance = this;
	}

	AndroidTestApplicationComponent testApplicationComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		testApplicationComponent = DaggerAndroidTestApplicationComponent.builder()
				.androidTestApplicationModule(new AndroidTestApplicationModule(this))
				.build();
		testApplicationComponent.inject(this);
	}

	public static Context getContext() {
		return instance;
	}

	public AndroidTestApplicationComponent getApplicationComponent() {
		return testApplicationComponent;
	}

	public static TestCompaniesHouseApplication getInstance() {
		return instance;
	}

}
