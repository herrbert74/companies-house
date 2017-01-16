package com.babestudios.companieshouse.ui.officers;

import android.app.Application;

import com.babestudios.companieshouse.DaggerTestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationModule;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.officers.Officers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfficersPresenterTest {

	@Mock
	Application application;

	@InjectMocks
	OfficersPresenter officersPresenter;

	@Before
	public void setUp() {
		TestApplicationComponent component = DaggerTestApplicationComponent.builder()
				.testApplicationModule(new TestApplicationModule(application)).build();
		component.inject(officersPresenter);
		officersPresenter = new OfficersPresenter(mock(DataManager.class));
		officersPresenter.create();
		OfficersActivityView view = mock(OfficersActivityView.class);
		officersPresenter.bindNewView(view);
		when(officersPresenter.dataManager.getOfficers(anyString(), any())).thenReturn(Observable.just(new Officers()));
	}

	@Test
	public void whenGetInsolvency_thenDataManagerGetInsolvencyIsCalled() {
		officersPresenter.getOfficers();
		verify(officersPresenter.dataManager).getOfficers(anyString(), any());
	}
}
