package com.babestudios.companyinfouk.ui.favourites

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
/*
@RunWith(MockitoJUnitRunner::class)
class FavouritesPresenterTest {


	private lateinit var favouritesPresenter: FavouritesPresenter

	@Before
	fun setUp() {
		val mockCompaniesRepository: CompaniesRepository = mock()
		val view: FavouritesActivityView = mock()
		favouritesPresenter = FavouritesPresenter(mockCompaniesRepository)
		favouritesPresenter.create()
		`when`<Array<SearchHistoryItem>>(mockCompaniesRepository.favourites).thenReturn(arrayOf(SearchHistoryItem("", "", 0L)))
		favouritesPresenter.attachView(view)
	}

	@Test
	fun onAttachView_shouldCallShowFavouritesOnView() {
		verify(favouritesPresenter.view!!).showFavourites(any())
	}

	@Test
	fun whenGetCompany_shouldCallStartCompanyActivityOnView() {
		favouritesPresenter.getCompany("", "")
		verify(favouritesPresenter.view!!).startCompanyActivity(any(), any())
	}

	@Test
	fun whenRemoveFavourite_shouldCallDataManagerRemoveFavourite() {
		favouritesPresenter.removeFavourite(SearchHistoryItem("", "", 0L))
		verify(favouritesPresenter.companiesRepository).removeFavourite(any())
	}
}
*/