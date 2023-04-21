package com.babestudios.companyinfouk.common.ext

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.babestudios.base.android.R

fun AppCompatActivity.startActivityWithRightSlide(intent: Intent) {
	this.startActivity(intent)
	this.overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out)
}
