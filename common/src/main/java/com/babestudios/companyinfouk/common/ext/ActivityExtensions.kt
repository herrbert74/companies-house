package com.babestudios.companyinfouk.common.ext

import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.babestudios.companyinfouk.common.R

fun AppCompatActivity.startActivityWithRightSlide(intent: Intent) {
	this.startActivity(intent)
	this.overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out)
}

fun AppCompatActivity.startActivityForResultWithRightSlide(intent: Intent, requestCode: Int) {
	this.startActivityForResult(intent, requestCode)
	this.overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out)
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
	lazy(LazyThreadSafetyMode.NONE) {
		bindingInflater.invoke(layoutInflater)
	}
