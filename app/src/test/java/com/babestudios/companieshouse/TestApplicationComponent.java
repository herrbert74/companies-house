package com.babestudios.companieshouse;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.injection.ApplicationComponent;
import com.babestudios.companieshouse.ui.company.CompanyPresenter;
import com.babestudios.companieshouse.ui.company.CompanyPresenterTest;
import com.babestudios.companieshouse.ui.search.SearchPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {TestApplicationModule.class})
public interface TestApplicationComponent extends ApplicationComponent {
	void inject(CompanyPresenterTest companyPresenterTest);
	void inject(CompanyPresenter companyPresenter);
	void inject(SearchPresenter searchPresenter);

	DataManager dataManager();
}
