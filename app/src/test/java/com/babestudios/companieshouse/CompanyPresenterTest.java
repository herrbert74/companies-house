package com.babestudios.companieshouse;

import android.app.Application;

import com.babestudios.companieshouse.data.model.company.Company;
import com.babestudios.companieshouse.ui.company.CompanyPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CompanyPresenterTest {

	@Mock
	Application application;

	@InjectMocks
	CompanyPresenter companyPresenter;

	@Before
	public void setUp() {
		companyPresenter = new CompanyPresenter();

		TestApplicationComponent component = DaggerTestApplicationComponent.builder()
				.testApplicationModule(new TestApplicationModule(application)).build();
		component.inject(companyPresenter);
		doThrow(new RuntimeException())
				.when(companyPresenter.dataManager)
				.addFavourite(any());
		companyPresenter.company = new Company();
	}

	@Test
	public void testWhenFabClickedDataManagerAddFavouriteIsCalled() {
		companyPresenter.onFabClicked();
		verify(companyPresenter.dataManager).addFavourite(any());
	}
}
