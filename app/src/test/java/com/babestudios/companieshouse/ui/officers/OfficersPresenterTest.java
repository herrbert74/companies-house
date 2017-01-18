package com.babestudios.companieshouse.ui.officers;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.officers.Officers;

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
public class OfficersPresenterTest {

	private OfficersPresenter officersPresenter;

	@Before
	public void setUp() {
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
