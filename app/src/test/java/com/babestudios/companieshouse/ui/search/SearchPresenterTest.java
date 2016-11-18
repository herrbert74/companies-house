package com.babestudios.companieshouse.ui.search;

import android.app.Application;

import com.babestudios.companieshouse.DaggerTestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationModule;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.search.CompanySearchResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchPresenterTest {

	@Mock
	Application application;

	@InjectMocks
	SearchPresenter searchPresenter;

	private SearchActivityView view;

	@Before
	public void setUp() {
		TestApplicationComponent component = DaggerTestApplicationComponent.builder().testApplicationModule(new TestApplicationModule
				(application)).build();
		component.inject(searchPresenter);
		searchPresenter = new SearchPresenter(Mockito.mock(DataManager.class));
		searchPresenter.create();
		view = mock(SearchActivityView.class);
		searchPresenter.bindNewView(view);
		when(searchPresenter.dataManager.searchCompanies(any(), any())).thenReturn(Observable.just(new CompanySearchResult()));
	}

	@Test
	public void test_When_FabClicked_Then_View_ShowDeleteRecentSearchesDialogIsCalled() {
		searchPresenter.onFabClicked();
		verify(view).showDeleteRecentSearchesDialog();
	}

	@Test
	public void test_When_GetCompany_Then_DataManagerGetCompanyIsCalled() {
		searchPresenter.getCompany("", "");
		verify(view).startCompanyActivity(anyString());
		verify(view).refreshRecentSearchesAdapter(any());
	}


	@Test
	public void test_When_Search_Then_DataManagerSearchCompaniesIsCalled() {
		searchPresenter.search("");
		verify(searchPresenter.dataManager).searchCompanies(any(), any());
	}

	@Test
	public void test_When_SearchLoadMore_Then_DataManagerSearchCompaniesIsCalled() {
		searchPresenter.search("");
		verify(searchPresenter.dataManager).searchCompanies(any(), any());
	}


}
