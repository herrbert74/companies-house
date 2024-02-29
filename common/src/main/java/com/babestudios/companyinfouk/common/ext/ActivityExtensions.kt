package com.babestudios.companyinfouk.common.ext

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.babestudios.base.android.R

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Suppress("DEPRECATION")
fun ComponentActivity.startActivityWithRightSlide(intent: Intent) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
		this.overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, R.anim.right_slide_in, R.anim.left_slide_out)
	} else {
		this.overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out)
	}
	this.startActivity(intent)
}
