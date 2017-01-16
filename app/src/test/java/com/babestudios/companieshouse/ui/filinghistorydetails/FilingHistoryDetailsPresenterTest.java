package com.babestudios.companieshouse.ui.filinghistorydetails;

import android.app.Application;

import com.babestudios.companieshouse.DaggerTestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationModule;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.filinghistory.FilingHistoryList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

	@Mock
	Application application;

	@InjectMocks
	FilingHistoryDetailsPresenter filingHistoryDetailsPresenter;

	@Before
	public void setUp() {
		TestApplicationComponent component = DaggerTestApplicationComponent.builder()
				.testApplicationModule(new TestApplicationModule(application)).build();
		component.inject(filingHistoryDetailsPresenter);
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
