package com.babestudios.companieshouse.ui.officers;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.officers.Officers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfficersPresenterTest {

	private OfficersPresenter officersPresenter;

	@Before
	public void setUp() {
		officersPresenter = new OfficersPresenter(mock(DataManager.class));
		officersPresenter.create();
		OfficersActivityView view = mock(OfficersActivityView.class);
		officersPresenter.bindNewView(view);
		when(view.getCompanyNumber()).thenReturn("0");
		when(officersPresenter.dataManager.getOfficers("0", "0")).thenReturn(Observable.just(new Officers()));
	}

	@Test
	public void whenGetInsolvency_thenDataManagerGetInsolvencyIsCalled() {
		officersPresenter.getOfficers();
		verify(officersPresenter.dataManager).getOfficers("0", "0");
	}
}
