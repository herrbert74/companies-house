package com.babestudios.companyinfouk.ui.officers;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.officers.Officers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfficersPresenterTest {

	private OfficersPresenter officersPresenter;

	@Before
	public void setUp() {
		officersPresenter = new OfficersPresenter(mock(DataManager.class));
		OfficersActivityView view = mock(OfficersActivityView.class);
		when(view.getCompanyNumber()).thenReturn("0");
		when(officersPresenter.dataManager.getOfficers("0", "0")).thenReturn(Observable.just(new Officers()));
		officersPresenter.create();
		officersPresenter.attachView(view);
	}

	@Test
	public void whenGetInsolvency_thenDataManagerGetInsolvencyIsCalled() {
		officersPresenter.getOfficers();
		verify(officersPresenter.dataManager, times(2)).getOfficers("0", "0");
	}
}
