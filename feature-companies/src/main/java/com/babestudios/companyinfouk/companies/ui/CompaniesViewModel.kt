package com.babestudios.companyinfouk.companies.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.babestudios.base.ext.biLet
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.companyinfouk.domain.model.company.Company
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesListItem
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryHeaderItem
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryHeaderVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitableBase
import com.babestudios.companyinfouk.companies.ui.main.search.SearchVisitable
import com.babestudios.companyinfouk.companies.ui.main.search.SearchVisitableBase
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.navigation.features.CompaniesNavigator
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class CompaniesViewModel(
	companiesState: CompaniesState,
	private val companiesRepository: CompaniesRepository,
	var companiesNavigator: CompaniesNavigator,
	private val recentSearchesString: String
) : BaseViewModel<CompaniesState>(companiesState, companiesRepository) {

	companion object : MvRxViewModelFactory<CompaniesViewModel, CompaniesState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: CompaniesState): CompaniesViewModel? {
			val companiesRepository = viewModelContext.activity<CompaniesActivity>().injectCompaniesHouseRepository()
			val companiesNavigator = viewModelContext.activity<CompaniesActivity>().injectCompaniesNavigator()
			val recentSearchesString = viewModelContext.activity<CompaniesActivity>().injectRecentSearchesString()

			return CompaniesViewModel(
					state,
					companiesRepository,
					companiesNavigator,
					recentSearchesString
			)
		}

		override fun initialState(viewModelContext: ViewModelContext): CompaniesState? {
			val companyNumber = viewModelContext.activity<CompaniesActivity>().provideCompanyNumber()
			val companyName = viewModelContext.activity<CompaniesActivity>().provideCompanyName()
			val individualName = viewModelContext.activity<CompaniesActivity>().provideIndividualName()
			val individualAddress = viewModelContext.activity<CompaniesActivity>().provideIndividualAddress()
			return when {
				companyNumber.isNotEmpty() -> CompaniesState(companyNumber = companyNumber, companyName = companyName)
				individualName != null ->
					CompaniesState(individualName = individualName, individualAddress = individualAddress)
				else -> null
			}
		}
	}

	fun setNavigator(navigator: CompaniesNavigator) {
		companiesNavigator = navigator
	}

	//region Recent searches

	fun showRecentSearches() {
		companiesRepository.recentSearches()
				.execute {
					copy(
							searchHistoryRequest = it,
							searchHistoryVisitables = convertSearchHistoryToVisitables(it() ?: emptyList())
					)
				}
	}

	private fun convertSearchHistoryToVisitables(reply: List<SearchHistoryItem>):
			List<SearchHistoryVisitableBase> {
		val searchHistoryVisitables: MutableList<SearchHistoryVisitableBase> =
				reply.map { item ->
					SearchHistoryVisitable(
						SearchHistoryItem(
							item.companyName,
							item.companyNumber,
							System.currentTimeMillis()
					)
					)
				}.toMutableList()
		if (searchHistoryVisitables.size > 0)
			searchHistoryVisitables.add(
					0,
					SearchHistoryHeaderVisitable(SearchHistoryHeaderItem(recentSearchesString))
			)
		return searchHistoryVisitables
	}

	fun clearAllRecentSearches() {
		companiesRepository.clearAllRecentSearches()
		setState {
			copy(
					searchRequest = Success(CompanySearchResult()),
					searchHistoryVisitables = convertSearchHistoryToVisitables(emptyList())
			)
		}
	}

	fun searchHistoryItemClicked(adapterPosition: Int) {
		withState { state ->
			val searchHistoryItem = (state.searchHistoryVisitables[adapterPosition] as SearchHistoryVisitable).searchHistoryItem
			setState {
				copy(
						companyName = searchHistoryItem.companyName,
						companyNumber = searchHistoryItem.companyNumber
				)
			}
		}
		companiesNavigator.mainToCompany()
	}

	//endregion

	//region Search

	fun setSearchMenuItemExpanded(expanded: Boolean) {
		setState {
			copy(
					isSearchMenuItemExpanded = expanded
			)
		}
	}

	fun onSearchQueryChanged(queryText: String) {
		withState {
			when {
				isComingBackFromCompanyScreen(it.queryText, queryText) -> return@withState
				queryText.length > 2 -> search(queryText)
				else -> {
					setState {
						copy(
								timeStamp = System.currentTimeMillis(),
								queryText = queryText,
								searchRequest = Uninitialized,
								searchVisitables = emptyList(),
								filteredSearchVisitables = emptyList()
						)
					}
				}
			}
		}
	}

	private fun isComingBackFromCompanyScreen(stateQueryText: String, newQueryText: String) =
			stateQueryText.isNotEmpty() && stateQueryText == newQueryText

	fun search(queryText: String) {
		companiesRepository.searchCompanies(queryText, "0")
				.execute {
					copy(
							timeStamp = System.currentTimeMillis(),
							queryText = queryText,
							totalCount = it()?.totalResults ?: 0,
							searchRequest = it,
							searchVisitables =
							convertSearchResultsToVisitables(it() ?: CompanySearchResult()),
							filteredSearchVisitables = filterSearchResults(filterState,
									convertSearchResultsToVisitables(it() ?: CompanySearchResult()))
									.blockingGet()
					)
				}
	}

	fun loadMoreSearch(page: Int) {
		withState { state ->
			if (state.searchVisitables.size < state.totalCount) {
				companiesRepository.searchCompanies(
						state.queryText,
						(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()
				)
						.execute {
							copy(
									timeStamp = System.currentTimeMillis(),
									queryText = queryText,
									totalCount = it()?.totalResults ?: 0,
									searchRequest = it,
									searchVisitables = convertLoadMoreSearchResultsToVisitables(
											state.searchVisitables,
											it() ?: CompanySearchResult()
									),
									filteredSearchVisitables = filterSearchResults(filterState,
											convertLoadMoreSearchResultsToVisitables(
													state.searchVisitables,
													it() ?: CompanySearchResult()
											))
											.blockingGet()
							)
						}
			}
		}
	}

	private fun convertSearchResultsToVisitables(reply: CompanySearchResult): List<SearchVisitableBase> {
		return ArrayList(reply.items.map { item -> SearchVisitable(item) })
	}

	private fun convertLoadMoreSearchResultsToVisitables(
			currentSearchVisitables: List<SearchVisitableBase>,
			reply: CompanySearchResult
	): List<SearchVisitableBase> {
		val updatedList = currentSearchVisitables.toMutableList()
		updatedList.addAll(reply.items.map { item -> SearchVisitable(item) })
		return updatedList
	}

	private fun filterSearchResults(
			filterState: FilterState?,
			searchVisitables: List<SearchVisitableBase>)
			: Single<List<SearchVisitableBase>> {
		return Observable.fromIterable(searchVisitables)
				.filter { companySearchResultItem ->
					val searchItem = (companySearchResultItem as SearchVisitable).searchItem
					filterState == FilterState.FILTER_SHOW_ALL ||
							(searchItem.companyStatus != null
									&& searchItem.companyStatus.equals(filterState.toString(), ignoreCase = true))
				}
				.observeOn(Schedulers.trampoline())
				.toList()
	}

	fun logSearch() {
		withState { companiesRepository.logSearch(it.queryText) }
	}

	//endregion

	//region main actions

	fun searchItemClicked(name: String, number: String) {
		//TODO Simply pass the adapter position, so it can be tested?
		val searchHistoryItems = companiesRepository.addRecentSearchItem(
				SearchHistoryItem(
						name,
						number,
						System.currentTimeMillis()
				)
		)
		setState {
			copy(
					companyName = name,
					companyNumber = number,
					searchHistoryVisitables = convertSearchHistoryToVisitables(searchHistoryItems)
			)
		}
		companiesNavigator.mainToCompany()
	}

	fun clearSearch() {
		setState {
			copy(
					queryText = "",
					searchRequest = Uninitialized,
					searchVisitables = emptyList(),
					filteredSearchVisitables = emptyList()
			)
		}
	}

	fun setFilterState(filterState: FilterState) {
		withState { state ->
			if (filterState.ordinal > FilterState.FILTER_SHOW_ALL.ordinal) {
				filterSearchResults(filterState, state.searchVisitables)
						.subscribe { result ->
							setState {
								copy(
										filteredSearchVisitables = result,
										filterState = filterState
								)
							}
						}
			} else {
				setState {
					copy(
							filteredSearchVisitables = state.searchVisitables,
							filterState = filterState
					)
				}
			}
		}
	}

	//endregion

	//region company

	fun fetchCompany(companyNumber: String) {
		companiesRepository.getCompany(companyNumber)
				.execute {
					copy(
							isFavorite = companiesRepository.isFavourite(
								SearchHistoryItem(
									this.companyName,
									this.companyNumber,
									0
							)
							),
							companyRequest = it,
							company = it() ?: Company(),
					)
				}
	}

	fun flipCompanyFavoriteState() {
		withState { state ->
			(state.companyName to state.companyNumber).biLet { companyName, companyNumber ->
				if (companiesRepository.isFavourite(SearchHistoryItem(companyName, companyNumber, 0))) {
					companiesRepository.removeFavourite(SearchHistoryItem(companyName, companyNumber, 0))
				} else {
					companiesRepository.addFavourite(SearchHistoryItem(companyName, companyNumber, 0))
				}
				setState {
					copy(
							isFavorite = companiesRepository.isFavourite(
								SearchHistoryItem(
									this.companyName,
									this.companyNumber,
									0
							)
							)
					)
				}
			}
		}
	}

	//endregion

	//region Favourites

	fun loadFavourites() {
		companiesRepository.favourites()
				.execute {
					copy(
							favouritesRequest = it,
							favouriteItems = convertToVisitables(it() ?: emptyList())
					)
				}
	}

	private fun convertToVisitables(favourites: List<SearchHistoryItem>): List<FavouritesVisitable> {
		return ArrayList(favourites.map { item -> FavouritesVisitable(FavouritesListItem(item)) })
	}

	fun favouritesItemClicked(adapterPosition: Int) {
		withState { state ->
			state.favouriteItems.let { favouriteItems ->
				val item = favouriteItems[adapterPosition]
						.favouritesListItem
						.searchHistoryItem
				setState {
					copy(
							companyName = item.companyName,
							companyNumber = item.companyNumber
					)
				}
				companiesNavigator.favouritesToCompany(item.companyNumber, item.companyName)
			}
		}
	}

	fun removeFavourite(favouriteToRemove: SearchHistoryItem) {
		companiesRepository.removeFavourite(favouriteToRemove)
		loadFavourites()
	}

	//endregion

}
