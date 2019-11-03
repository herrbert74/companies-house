package com.babestudios.companyinfouk.ext

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R

fun AppCompatActivity.logScreenView(screenName: String) {
	val bundle = Bundle()
	bundle.putString("screen_name", screenName)
	CompaniesHouseApplication.instance.firebaseAnalytics?.logEvent("screenview", bundle)
}
