package com.babestudios.companieshouse.ui.insolvency;

import android.app.Application;

import com.babestudios.companieshouse.DaggerTestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationModule;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.insolvency.Insolvency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InsolvencyPresenterTest {

	@Mock
	Application application;

	@InjectMocks
	InsolvencyPresenter insolvencyPresenter;

	@Before
	public void setUp() {
		TestApplicationComponent component = DaggerTestApplicationComponent.builder()
				.testApplicationModule(new TestApplicationModule(application)).build();
		component.inject(insolvencyPresenter);
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
