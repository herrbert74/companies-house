package com.babestudios.companyinfouk;

import android.support.test.rule.ActivityTestRule;

import com.babestudios.companyinfouk.injection.ApplicationComponent;
import com.babestudios.companyinfouk.ui.favourites.FavouritesActivity;
import com.babestudios.companyinfouk.ui.favourites.FavouritesActivityTest;
import com.babestudios.companyinfouk.ui.search.SearchActivityTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AndroidTestApplicationModule.class})
public interface AndroidTestApplicationComponent extends ApplicationComponent {

	void inject(FavouritesActivityTest favouritesActivityTest);

	void inject(TestCompaniesHouseApplication testCompaniesHouseApplication);

	void inject(SearchActivityTest searchActivityTest);

	void inject(ActivityTestRule<FavouritesActivity> mActivityTestRule);
}
