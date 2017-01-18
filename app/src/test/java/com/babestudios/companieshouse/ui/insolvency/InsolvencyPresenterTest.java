package com.babestudios.companieshouse.ui.insolvency;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.insolvency.Insolvency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InsolvencyPresenterTest {

	private InsolvencyPresenter insolvencyPresenter;

	@Before
	public void setUp() {
		insolvencyPresenter = new InsolvencyPresenter(mock(DataManager.class));
		insolvencyPresenter.create();
		InsolvencyActivityView view = mock(InsolvencyActivityView.class);
		insolvencyPresenter.bindNewView(view);
		when(insolvencyPresenter.dataManager.getInsolvency(anyString())).thenReturn(Observable.just(new Insolvency()));
	}

	@Test
	public void test_When_GetInsolvency_Then_DataManagerGetInsolvencyIsCalled() {
		insolvencyPresenter.getInsolvency();
		verify(insolvencyPresenter.dataManager).getInsolvency(anyString());
	}
}
