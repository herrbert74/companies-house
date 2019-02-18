package com.babestudios.companyinfouk.injection

import androidx.test.rule.ActivityTestRule
import com.babestudios.companyinfouk.TestCompaniesHouseApplication
import com.babestudios.companyinfouk.ui.favourites.FavouritesActivity
import com.babestudios.companyinfouk.ui.favourites.FavouritesActivityTest
import com.babestudios.companyinfouk.ui.search.SearchActivityTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidTestApplicationModule::class])
interface AndroidTestApplicationComponent : ApplicationComponent {

	fun inject(favouritesActivityTest: FavouritesActivityTest)

	fun inject(testCompaniesHouseApplication: TestCompaniesHouseApplication)

	fun inject(searchActivityTest: SearchActivityTest)

	fun inject(mActivityTestRule: ActivityTestRule<FavouritesActivity>)
}
