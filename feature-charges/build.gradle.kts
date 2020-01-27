plugins{
	id("com.babestudios.companyinfouk.plugins.android")
	id("com.babestudios.companyinfouk.plugins.feature")
}

android {
	@Suppress("UnstableApiUsage")
	buildFeatures {
		viewBinding = true
	}
}
