package com.babestudios.companyinfouk.ui.search;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchPresenterTest {

	private SearchPresenter searchPresenter;

	private SearchActivityView view;

	private SearchHistoryItem searchHistoryItem = new SearchHistoryItem("TUI", "12344", 12L);

	@Before
	public void setUp() {
		searchPresenter = new SearchPresenter(Mockito.mock(DataManager.class));
		searchPresenter.create();
		view = mock(SearchActivityView.class);
		searchPresenter.attachView(view);
		when(searchPresenter.dataManager.searchCompanies(any(), anyString())).thenReturn(Observable.just(new CompanySearchResult()));
		ArrayList<SearchHistoryItem> searchHistoryItems = new ArrayList<>();
		SearchHistoryItem searchHistoryItem1 = new SearchHistoryItem("RUN", "12345", 12L);
		searchHistoryItems.add(searchHistoryItem);
		searchHistoryItems.add(searchHistoryItem1);
		when(searchPresenter.dataManager.addRecentSearchItem(searchHistoryItem)).thenReturn(searchHistoryItems);
	}

	@Test
	public void whenFabClickedInStateRecentSearches_thenShowDeleteRecentSearchesDialogIsCalled() {
		searchPresenter.onFabClicked();
		verify(view).showDeleteRecentSearchesDialog();
	}

	@Test
	public void whenFabClickedInStateSearch_thenClearSearchView_andRefreshAdapter_andShowRecentSearchesCalled() {
		searchPresenter.search("");
		searchPresenter.onFabClicked();
		verify(view).clearSearchView();
		verify(view).refreshRecentSearchesAdapter(any());
		verify(view, times(2)).showRecentSearches(any());
		verify(view, times(2)).changeFabImage(SearchPresenter.FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE);
	}

	@Test
	public void whenGetCompany_thenDataManagerAddRecentSearchItem_andStartActivityIsCalled() {
		searchPresenter.getCompany("TUI", "12344");
		verify(view).startCompanyActivity(anyString(), anyString());
		verify(searchPresenter.dataManager).addRecentSearchItem(searchHistoryItem);
	}

	@Test
	public void whenSearch_thenDataManagerSearchCompaniesIsCalled() {
		searchPresenter.search("");
		verify(searchPresenter.dataManager).searchCompanies(any(), anyString());
	}

	@Test
	public void whenSearchLoadMore_thenDataManagerSearchCompaniesIsCalled() {
		searchPresenter.search("");
		verify(searchPresenter.dataManager).searchCompanies(any(), anyString());
	}


}
