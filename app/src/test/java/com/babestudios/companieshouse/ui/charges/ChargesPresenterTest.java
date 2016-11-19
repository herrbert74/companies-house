package com.babestudios.companieshouse.ui.charges;

import android.app.Application;

import com.babestudios.companieshouse.DaggerTestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationModule;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.charges.Charges;

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
public class ChargesPresenterTest {

	@Mock
	Application application;

	@InjectMocks
	ChargesPresenter2 chargesPresenter;

	@Before
	public void setUp() {
		TestApplicationComponent component = DaggerTestApplicationComponent.builder()
				.testApplicationModule(new TestApplicationModule(application)).build();
		component.inject(chargesPresenter);
		chargesPresenter = new ChargesPresenter2(mock(DataManager.class));
		chargesPresenter.create();
		ChargesActivityView view = mock(ChargesActivityView.class);
		chargesPresenter.bindNewView(view);
		when(chargesPresenter.dataManager.getCharges(anyString(), any())).thenReturn(Observable.just(new Charges()));
	}

	@Test
	public void test_When_GetInsolvency_Then_DataManagerGetInsolvencyIsCalled() {
		chargesPresenter.getCharges();
		verify(chargesPresenter.dataManager).getCharges(anyString(), any());
	}
}
