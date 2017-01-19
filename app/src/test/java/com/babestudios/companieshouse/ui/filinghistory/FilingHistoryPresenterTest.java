package com.babestudios.companieshouse.ui.filinghistory;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.filinghistory.FilingHistoryList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilingHistoryPresenterTest {

	private FilingHistoryPresenter filingHistoryPresenter;

	@Before
	public void setUp() {
		filingHistoryPresenter = new FilingHistoryPresenter(mock(DataManager.class));
		filingHistoryPresenter.create();
		FilingHistoryActivityView view = mock(FilingHistoryActivityView.class);
		filingHistoryPresenter.bindNewView(view);
		when(filingHistoryPresenter.dataManager.getFilingHistory("23", "0", "0")).thenReturn(Observable.just(new FilingHistoryList()));
	}

	@Test
	public void whenGetFilingHistory_thenDataManagerGetFilingHistoryIsCalled() {
		filingHistoryPresenter.getFilingHistory("23", "0");
		verify(filingHistoryPresenter.dataManager).getFilingHistory("23", "0", "0");
	}
}
