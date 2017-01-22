package com.babestudios.companyinfouk;

import com.babestudios.companyinfouk.injection.ApplicationComponent;
import com.babestudios.companyinfouk.ui.favourites.FavouritesActivityTest;
import com.babestudios.companyinfouk.ui.search.SearchActivityTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {TestApplicationModule.class})
public interface TestApplicationComponent extends ApplicationComponent {

	void inject(FavouritesActivityTest favouritesActivityTest);

	void inject(TestCompaniesHouseApplication testCompaniesHouseApplication);

	void inject(SearchActivityTest searchActivityTest);
}
