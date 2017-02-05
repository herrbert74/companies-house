package com.babestudios.companyinfouk.uiplugins;

import android.os.Bundle;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.pascalwelsch.compositeandroid.activity.ActivityPlugin;

public class BaseActivityPlugin extends ActivityPlugin {

	public void logScreenView(String screenName) {
		Bundle bundle = new Bundle();
		bundle.putString("screen_name", screenName);
		CompaniesHouseApplication.getInstance().getFirebaseAnalytics().logEvent("screenview", bundle);
	}
}
