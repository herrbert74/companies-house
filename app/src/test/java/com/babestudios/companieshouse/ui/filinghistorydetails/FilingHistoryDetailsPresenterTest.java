package com.babestudios.companieshouse.ui.filinghistorydetails;

import com.babestudios.companieshouse.data.DataManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilingHistoryDetailsPresenterTest {

	private FilingHistoryDetailsPresenter filingHistoryDetailsPresenter;

	@Before
	public void setUp() {
		filingHistoryDetailsPresenter = new FilingHistoryDetailsPresenter(mock(DataManager.class));
		filingHistoryDetailsPresenter.create();
		FilingHistoryDetailsActivityView view = mock(FilingHistoryDetailsActivityView.class);
		filingHistoryDetailsPresenter.bindNewView(view);
		when(filingHistoryDetailsPresenter.dataManager.getDocument(anyString())).thenReturn(Observable.just(ResponseBody.create(MediaType.parse("text/plain"), "test")));
	}

	@Test
	public void whenGetDocument_thenDataManagerGetDocumentIsCalled() {
		filingHistoryDetailsPresenter.getDocument();
		verify(filingHistoryDetailsPresenter.dataManager).getDocument(anyString());
	}

	@Test
	public void whenWritePdf_thenDataManagerWriteDocumentPdfIsCalled() {
		filingHistoryDetailsPresenter.writePdf(ResponseBody.create(MediaType.parse("text/plain"), "test"));
		verify(filingHistoryDetailsPresenter.dataManager).writeDocumentPdf(any());
	}
}
