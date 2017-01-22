package com.babestudios.companyinfouk;

import android.content.Context;

public class TestCompaniesHouseApplication extends CompaniesHouseApplication {

	private static TestCompaniesHouseApplication instance;

	public TestCompaniesHouseApplication() {
		instance = this;
	}

	TestApplicationComponent testApplicationComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		testApplicationComponent = DaggerTestApplicationComponent.builder()
				.testApplicationModule(new TestApplicationModule(this))
				.build();
		testApplicationComponent.inject(this);
	}

	public static Context getContext() {
		return instance;
	}

	public TestApplicationComponent getApplicationComponent() {
		return testApplicationComponent;
	}

	public static TestCompaniesHouseApplication getInstance() {
		return instance;
	}

}
