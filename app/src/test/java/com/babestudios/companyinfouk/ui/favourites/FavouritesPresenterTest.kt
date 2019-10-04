package com.babestudios.companyinfouk.ui.favourites

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