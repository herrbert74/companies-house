package com.babestudios.companyinfouk.uiplugins

import android.content.Intent
import android.os.Bundle

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.pascalwelsch.compositeandroid.activity.ActivityPlugin

class BaseActivityPlugin : ActivityPlugin() {

	fun logScreenView(screenName: String) {
		val bundle = Bundle()
		bundle.putString("screen_name", screenName)
		CompaniesHouseApplication.instance.firebaseAnalytics?.logEvent("screenview", bundle)
	}

	fun startActivityWithRightSlide(intent: Intent) {
		activity.startActivity(intent)
		activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}
}
