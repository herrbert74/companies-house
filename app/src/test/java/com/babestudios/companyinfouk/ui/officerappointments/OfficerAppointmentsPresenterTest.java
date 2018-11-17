package com.babestudios.companyinfouk.ui.officerappointments;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfficerAppointmentsPresenterTest {

	private OfficerAppointmentsPresenter officerAppointmentsPresenter;

	@Before
	public void setUp() {
		officerAppointmentsPresenter = new OfficerAppointmentsPresenter(mock(DataManager.class));
		OfficerAppointmentsActivityView view = mock(OfficerAppointmentsActivityView.class);
		when(view.getOfficerId()).thenReturn("0");
		when(officerAppointmentsPresenter.dataManager.getOfficerAppointments("0", "0")).thenReturn(Observable.just(new Appointments()));
		officerAppointmentsPresenter.create();
		officerAppointmentsPresenter.attachView(view);
	}

	@Test
	public void whenGetAppointments_thenDataManagerGetAppointmentsIsCalled() {
		officerAppointmentsPresenter.getAppointments();
		verify(officerAppointmentsPresenter.dataManager, times(2)).getOfficerAppointments("0", "0");
	}

	@Test
	public void whenOnNext_thenViewHideProgressAndShowAppointmentsIsCalled() {
		officerAppointmentsPresenter.onNext(new Appointments());
		verify(officerAppointmentsPresenter.getView(), times(2)).hideProgress();
		verify(officerAppointmentsPresenter.getView(), times(2)).showAppointments(any(Appointments.class));
	}
}
