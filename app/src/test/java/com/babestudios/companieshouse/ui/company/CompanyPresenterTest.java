package com.babestudios.companieshouse.ui.company;

import android.app.Application;

import com.babestudios.companieshouse.DaggerTestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationModule;
import com.babestudios.companieshouse.data.model.company.Company;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompanyPresenterTest {

	@Mock
	Application application;

	@InjectMocks
	CompanyPresenter companyPresenter;

	@Before
	public void setUp() {
		//companyPresenter = new CompanyPresenter();

		TestApplicationComponent component = DaggerTestApplicationComponent.builder()
				.testApplicationModule(new TestApplicationModule(application)).build();
		component.inject(companyPresenter);
		when(companyPresenter.dataManager.getCompany(any())).thenReturn(Observable.just(new Company()));
		companyPresenter.company = new Company();
	}

	@Test
	public void test_When_FabClicked_Then_DataManagerAddFavouriteIsCalled() {
		companyPresenter.onFabClicked();
		verify(companyPresenter.dataManager).addFavourite(any());
	}

	@Test
	public void test_When_GetCompany_Then_DataManagerGetCompanyIsCalled() {
		companyPresenter.getCompany("");
		verify(companyPresenter.dataManager).getCompany(any());
	}
}
