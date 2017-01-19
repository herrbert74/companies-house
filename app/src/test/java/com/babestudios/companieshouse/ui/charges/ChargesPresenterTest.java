package com.babestudios.companieshouse.ui.charges;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.charges.Charges;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;

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
		when(view.getCompanyNumber()).thenReturn("0");
		chargesPresenter.bindNewView(view);
		when(chargesPresenter.dataManager.getCharges("0", "0")).thenReturn(Observable.just(new Charges()));
	}

	@Test
	public void whenGetInsolvency_thenDataManagerGetInsolvencyIsCalled() {
		chargesPresenter.getCharges();
		verify(chargesPresenter.dataManager).getCharges("0", "0");
	}

	@Test
	public void whenLoadMoreCharges_thenDataManagerLoadMoreChargesIsCalled() {
		chargesPresenter.loadMoreCharges(0);
		verify(chargesPresenter.dataManager).getCharges("0", "0");
	}
}
