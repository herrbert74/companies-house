package com.babestudios.companyinfouk.ui.privacy

import android.os.Bundle
import android.webkit.WebView

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.pascalwelsch.compositeandroid.activity.CompositeActivity

class PrivacyActivity : CompositeActivity() {


	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val view = WebView(this)
		view.settings.javaScriptEnabled = true
		view.loadUrl("file:///android_asset/privacy_policy.html")
		setContentView(view)
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
