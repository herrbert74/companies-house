package com.babestudios.companieshouse.ui.filinghistory;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.filinghistory.FilingHistoryList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
		when(filingHistoryPresenter.dataManager.getFilingHistory(anyString(), anyString(), anyString())).thenReturn(Observable.just(new FilingHistoryList()));
	}

	@Test
	public void whenGetFilingHistory_thenDataManagerGetFilingHistoryIsCalled() {
		filingHistoryPresenter.getFilingHistory();
		verify(filingHistoryPresenter.dataManager).getFilingHistory(anyString(), any(), any());
	}
}
