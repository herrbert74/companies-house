package com.babestudios.companyinfouk.ui.favourites;

import com.babestudios.companyinfouk.data.DataManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FavouritesPresenterTest {


	FavouritesPresenter favouritesPresenter;

	@Before
	public void setUp() {
		favouritesPresenter = new FavouritesPresenter(mock(DataManager.class));
		favouritesPresenter.create();
		FavouritesActivityView view = mock(FavouritesActivityView.class);
		favouritesPresenter.attachView(view);
	}

	@Test
	public void onAttachView_shouldCallShowFavouritesOnView() {
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
