package com.babestudios.companyinfouk.uiplugins;

import android.content.Intent;
import android.os.Bundle;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.pascalwelsch.compositeandroid.activity.ActivityPlugin;

public class BaseActivityPlugin extends ActivityPlugin {

	public void logScreenView(String screenName) {
		Bundle bundle = new Bundle();
		bundle.putString("screen_name", screenName);
		CompaniesHouseApplication.getInstance().getFirebaseAnalytics().logEvent("screenview", bundle);
	}

	public void startActivityWithRightSlide(Intent intent) {
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	}
}
