package com.babestudios.companyinfouk.ui.filinghistorydetails;

import android.net.Uri;

import com.babestudios.companyinfouk.data.DataManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import rx.Observable;

import static org.mockito.ArgumentMatchers.any;
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
		when(view.getFilingHistoryItemString()).thenReturn("0");
		filingHistoryDetailsPresenter.attachView(view);
		when(filingHistoryDetailsPresenter.dataManager.getDocument("0")).thenReturn(Observable.just(ResponseBody.create(MediaType.parse("text/plain"), "test")));
		when(filingHistoryDetailsPresenter.dataManager.writeDocumentPdf(any(ResponseBody.class))).thenReturn(Uri.parse("http:\\some.com"));
	}

	@Test
	public void whenGetDocument_thenDataManagerGetDocumentIsCalled() {
		filingHistoryDetailsPresenter.getDocument();
		verify(filingHistoryDetailsPresenter.dataManager).getDocument("0");
	}

	@Test
	public void whenWritePdf_thenDataManagerWriteDocumentPdfIsCalled() {
		filingHistoryDetailsPresenter.writePdf(ResponseBody.create(MediaType.parse("text/plain"), "test"));
		verify(filingHistoryDetailsPresenter.dataManager).writeDocumentPdf(any(ResponseBody.class));
	}
}
