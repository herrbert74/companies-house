package com.babestudios.companyinfouk;

import com.babestudios.companyinfouk.injection.ApplicationComponent;
import com.babestudios.companyinfouk.ui.search.SearchPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {TestApplicationModule.class})
public interface TestApplicationComponent extends ApplicationComponent {

	void inject(SearchPresenter searchPresenter);

}
