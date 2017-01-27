package com.babestudios.companyinfouk.ui.persons;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.persons.Persons;

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
public class PersonsPresenterTest {

	private PersonsPresenter personsPresenter;

	@Before
	public void setUp() {
		personsPresenter = new PersonsPresenter(mock(DataManager.class));
		PersonsActivityView view = mock(PersonsActivityView.class);
		when(view.getCompanyNumber()).thenReturn("0");
		when(personsPresenter.dataManager.getPersons("0", "0")).thenReturn(Observable.just(new Persons()));
		personsPresenter.create();
		personsPresenter.attachView(view);
	}

	@Test
	public void test_When_GetInsolvency_Then_DataManagerGetInsolvencyIsCalled() {
		personsPresenter.getPersons();
		verify(personsPresenter.dataManager, times(2)).getPersons("0", "0");
	}
}
