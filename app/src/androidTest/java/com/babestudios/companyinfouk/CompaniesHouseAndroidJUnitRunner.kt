package com.babestudios.companyinfouk

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * Use our TestApplication to inject dependencies
 */

class CompaniesHouseAndroidJUnitRunner  : AndroidJUnitRunner() {

        @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
        override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
            val testApplicationClassName = TestCompaniesHouseApplication::class.java.canonicalName
            return super.newApplication(cl, testApplicationClassName, context)
        }
    }