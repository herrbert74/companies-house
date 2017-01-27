package com.babestudios.companyinfouk.ui.filinghistory;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilingHistoryPresenterTest {

	private FilingHistoryPresenter filingHistoryPresenter;

	@Before
	public void setUp() {
		filingHistoryPresenter = new FilingHistoryPresenter(mock(DataManager.class));
		FilingHistoryActivityView view = mock(FilingHistoryActivityView.class);
		when(view.getCompanyNumber()).thenReturn("23");
		when(view.getFilingCategory()).thenReturn("0");
		when(filingHistoryPresenter.dataManager.getFilingHistory("23", "0", "0")).thenReturn(Observable.just(new FilingHistoryList()));
		filingHistoryPresenter.create();
		filingHistoryPresenter.attachView(view);
	}

	@Test
	public void whenGetFilingHistory_thenDataManagerGetFilingHistoryIsCalled() {
		filingHistoryPresenter.getFilingHistory("23", "0");
		verify(filingHistoryPresenter.dataManager, times(2)).getFilingHistory("23", "0", "0");
	}
}
