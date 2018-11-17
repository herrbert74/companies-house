package com.babestudios.companyinfouk.ui.charges;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.charges.Charges;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChargesPresenterTest {

	private ChargesPresenter chargesPresenter;

	@Before
	public void setUp() {
		chargesPresenter = new ChargesPresenter(mock(DataManager.class));
		ChargesActivityView view = mock(ChargesActivityView.class);
		when(view.getCompanyNumber()).thenReturn("0");
		when(chargesPresenter.getDataManager().getCharges("0", "0")).thenReturn(Observable.just(new Charges()));
		chargesPresenter.create();
		chargesPresenter.attachView(view);

	}

	@Test
	public void whenGetInsolvency_thenDataManagerGetInsolvencyIsCalled() {
		chargesPresenter.getCharges();
		verify(chargesPresenter.getDataManager(), times(2)).getCharges("0", "0");
	}

	@Test
	public void whenLoadMoreCharges_thenDataManagerLoadMoreChargesIsCalled() {
		chargesPresenter.loadMoreCharges(0);
		verify(chargesPresenter.getDataManager(), times(2)).getCharges("0", "0");
	}
}
