package com.babestudios.companieshouse.ui.persons;

import android.app.Application;

import com.babestudios.companieshouse.DaggerTestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationModule;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.insolvency.Insolvency;
import com.babestudios.companieshouse.data.model.persons.Persons;

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
public class PersonsPresenterTest {

	@Mock
	Application application;

	@InjectMocks
	PersonsPresenter personsPresenter;

	@Before
	public void setUp() {
		TestApplicationComponent component = DaggerTestApplicationComponent.builder()
				.testApplicationModule(new TestApplicationModule(application)).build();
		component.inject(personsPresenter);
		personsPresenter = new PersonsPresenter(mock(DataManager.class));
		personsPresenter.create();
		PersonsActivityView view = mock(PersonsActivityView.class);
		personsPresenter.bindNewView(view);
		when(personsPresenter.dataManager.getPersons(anyString(), any())).thenReturn(Observable.just(new Persons()));
	}

	@Test
	public void test_When_GetInsolvency_Then_DataManagerGetInsolvencyIsCalled() {
		personsPresenter.getPersons();
		verify(personsPresenter.dataManager).getPersons(anyString(), any());
	}
}
