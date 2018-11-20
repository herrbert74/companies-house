package com.babestudios.companyinfouk.ui.favourites

import com.babestudios.companyinfouk.data.DataManager

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.junit.MockitoJUnitRunner

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class FavouritesPresenterTest {


	private var favouritesPresenter: FavouritesPresenter? = null

	@Before
	fun setUp() {
		favouritesPresenter = FavouritesPresenter(mock(DataManager::class.java))
		favouritesPresenter?.create()
		val view = mock(FavouritesActivityView::class.java)
		favouritesPresenter?.attachView(view)
	}

	@Test
	fun onAttachView_shouldCallShowFavouritesOnView() {
		verify<FavouritesActivityView>(favouritesPresenter?.view).showFavourites(ArgumentMatchers.any())
	}

	@Test
	fun whenGetCompany_shouldCallStartCompanyActivityOnView() {
		favouritesPresenter?.getCompany(ArgumentMatchers.any(), ArgumentMatchers.any())
		verify<FavouritesActivityView>(favouritesPresenter?.view).startCompanyActivity(ArgumentMatchers.any(), ArgumentMatchers.any())
	}

	@Test
	fun whenRemoveFavourite_shouldCallDataManagerRemoveFavourite() {
		favouritesPresenter?.removeFavourite(ArgumentMatchers.any())
		verify(favouritesPresenter?.dataManager)?.removeFavourite(ArgumentMatchers.any())
	}
}
