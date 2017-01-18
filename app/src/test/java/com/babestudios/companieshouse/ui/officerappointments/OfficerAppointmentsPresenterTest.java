package com.babestudios.companieshouse.ui.officerappointments;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.officers.appointments.Appointments;

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
public class OfficerAppointmentsPresenterTest {

	private OfficerAppointmentsPresenter officerAppointmentsPresenter;

	@Before
	public void setUp() {
		officerAppointmentsPresenter = new OfficerAppointmentsPresenter(mock(DataManager.class));
		officerAppointmentsPresenter.create();
		OfficerAppointmentsActivityView view = mock(OfficerAppointmentsActivityView.class);
		officerAppointmentsPresenter.bindNewView(view);
		when(officerAppointmentsPresenter.dataManager.getOfficerAppointments(anyString(), any())).thenReturn(Observable.just(new Appointments()));
	}

	@Test
	public void whenGetAppointments_thenDataManagerGetAppointmentsIsCalled() {
		officerAppointmentsPresenter.getAppointments();
		verify(officerAppointmentsPresenter.dataManager).getOfficerAppointments(anyString(), any());
	}

	@Test
	public void whenOnNext_thenViewHideProgressAndShowAppointmentsIsCalled() {
		officerAppointmentsPresenter.onNext(new Appointments());
		verify(officerAppointmentsPresenter.getView()).hideProgress();
		verify(officerAppointmentsPresenter.getView()).showAppointments(any());
	}
}
