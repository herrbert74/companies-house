package com.babestudios.companieshouse.ui.charges;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.charges.Charges;

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
public class ChargesPresenterTest {

	ChargesPresenter chargesPresenter;

	@Before
	public void setUp() {
		chargesPresenter = new ChargesPresenter(mock(DataManager.class));
		chargesPresenter.create();
		ChargesActivityView view = mock(ChargesActivityView.class);
		chargesPresenter.bindNewView(view);
		when(chargesPresenter.dataManager.getCharges(anyString(), any())).thenReturn(Observable.just(new Charges()));
	}

	@Test
	public void whenGetInsolvency_thenDataManagerGetInsolvencyIsCalled() {
		chargesPresenter.getCharges();
		verify(chargesPresenter.dataManager).getCharges(anyString(), any());
	}

	@Test
	public void whenLoadMoreCharges_thenDataManagerLoadMoreChargesIsCalled() {
		chargesPresenter.loadMoreCharges(0);
		verify(chargesPresenter.dataManager).getCharges(anyString(), any());
	}
}
