package com.babestudios.companieshouse.ui.officerappointments;

import android.app.Application;

import com.babestudios.companieshouse.DaggerTestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationModule;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.officers.appointments.Appointments;

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
public class OfficerAppointmentsPresenterTest {

	@Mock
	Application application;

	@InjectMocks
	OfficerAppointmentsPresenter officerAppointmentsPresenter;

	@Before
	public void setUp() {
		TestApplicationComponent component = DaggerTestApplicationComponent.builder()
				.testApplicationModule(new TestApplicationModule(application)).build();
		component.inject(officerAppointmentsPresenter);
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
