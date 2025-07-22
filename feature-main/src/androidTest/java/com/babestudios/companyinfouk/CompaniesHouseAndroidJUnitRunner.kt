package com.babestudios.companyinfouk

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * Use our TestApplication to inject dependencies
 */

@Suppress("unused")
class CompaniesHouseAndroidJUnitRunner : AndroidJUnitRunner() {

	override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
		return super.newApplication(cl, TestApplication::class.java.name, context)
	}

}
