package com.babestudios.companieshouse.ui.favourites;

import android.app.Application;

import com.babestudios.companieshouse.DaggerTestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationComponent;
import com.babestudios.companieshouse.TestApplicationModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FavouritesPresenterTest {

	@Mock
	Application application;

	@InjectMocks
	FavouritesPresenter favouritesPresenter;

	@Mock
	FavouritesActivity favouritesActivity;

	@Before
	public void setUp() {
		TestApplicationComponent component = DaggerTestApplicationComponent.builder()
				.testApplicationModule(new TestApplicationModule(application)).build();
		component.inject(favouritesPresenter);
	}

	@Test
	public void whenOnResume_shouldCallShowFavouritesOnView() {
		favouritesPresenter.onResume();
		verify(favouritesPresenter.getView()).showFavourites(any());
	}

	@Test
	public void whenGetCompany_shouldCallStartCompanyActivityOnView() {
		favouritesPresenter.getCompany(any(), any());
		verify(favouritesPresenter.getView()).startCompanyActivity(any(), any());
	}

	@Test
	public void whenRemoveFavourite_shouldCallDataManagerRemoveFavourite() {
		favouritesPresenter.removeFavourite(any());
		verify(favouritesPresenter.dataManager).removeFavourite(any());
	}
}
