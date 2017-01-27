package com.babestudios.companyinfouk.ui.search;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchPresenterTest {

	private SearchPresenter searchPresenter;

	private SearchActivityView view;

	@Before
	public void setUp() {
		searchPresenter = new SearchPresenter(Mockito.mock(DataManager.class));
		searchPresenter.create();
		view = mock(SearchActivityView.class);
		searchPresenter.attachView(view);
		when(searchPresenter.dataManager.searchCompanies(any(), anyString())).thenReturn(Observable.just(new CompanySearchResult()));
	}

	@Test
	public void test_When_FabClicked_Then_View_ShowDeleteRecentSearchesDialogIsCalled() {

	}

	@Test
	public void test_When_GetCompany_Then_DataManagerGetCompanyIsCalled() {
		searchPresenter.getCompany("", "");
		verify(view).startCompanyActivity(anyString(), anyString());
	}

	@Test
	public void test_When_Search_Then_DataManagerSearchCompaniesIsCalled() {
		searchPresenter.search("");
		verify(searchPresenter.dataManager).searchCompanies(any(), anyString());
	}

	@Test
	public void test_When_SearchLoadMore_Then_DataManagerSearchCompaniesIsCalled() {
		searchPresenter.search("");
		verify(searchPresenter.dataManager).searchCompanies(any(), anyString());
	}


}
