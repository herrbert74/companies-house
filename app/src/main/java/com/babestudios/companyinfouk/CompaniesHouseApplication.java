package com.babestudios.companyinfouk;

import android.app.Application;
import android.content.Context;

import com.babestudios.companyinfouk.injection.ApplicationComponent;
import com.babestudios.companyinfouk.injection.ApplicationModule;
import com.babestudios.companyinfouk.injection.DaggerApplicationComponent;
import com.google.firebase.analytics.FirebaseAnalytics;

public class CompaniesHouseApplication extends Application {

	private static CompaniesHouseApplication instance;

	private FirebaseAnalytics firebaseAnalytics;

	public CompaniesHouseApplication() {
		instance = this;
	}

	ApplicationComponent mApplicationComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		firebaseAnalytics = FirebaseAnalytics.getInstance(this);
		logAppOpen();
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

	public FirebaseAnalytics getFirebaseAnalytics() {
		return firebaseAnalytics;
	}

	private void logAppOpen(){
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);

	}


}
