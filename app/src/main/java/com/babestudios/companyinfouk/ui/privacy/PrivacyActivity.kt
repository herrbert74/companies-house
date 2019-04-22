package com.babestudios.companyinfouk.ui.privacy

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.ext.logScreenView

class PrivacyActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val view = WebView(this)
		logScreenView(this.localClassName)
		view.settings.javaScriptEnabled = true
		view.loadUrl("file:///android_asset/privacy_policy.html")
		setContentView(view)
	}

	override fun onBackPressed() {
		super.onBackPressed()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
