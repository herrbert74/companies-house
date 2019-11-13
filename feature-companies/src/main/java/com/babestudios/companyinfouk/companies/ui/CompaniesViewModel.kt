package com.babestudios.companyinfouk.companies.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.babestudios.base.ext.biLet
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.base.mvrx.resolveErrorOrProceed
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesItem
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.AbstractSearchHistoryVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryHeaderItem
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryHeaderVisitable
import com.babestudios.companyinfouk.companies.ui.main.recents.SearchHistoryVisitable
import com.babestudios.companyinfouk.companies.ui.main.search.AbstractSearchVisitable
import com.babestudios.companyinfouk.companies.ui.main.search.SearchVisitable
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.navigation.features.CompaniesNavigator
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class CompaniesViewModel(
		companiesState: CompaniesState,
		private val companiesRepository: CompaniesRepositoryContract,
		val companiesNavigator: CompaniesNavigator,
		private val errorResolver: ErrorResolver,
		private val recentSearchesString: String
) : BaseViewModel<CompaniesState>(companiesState, companiesRepository) {

	companion object : MvRxViewModelFactory<CompaniesViewModel, CompaniesState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: CompaniesState): CompaniesViewModel? {
			val companiesRepository = viewModelContext.activity<CompaniesActivity>().injectCompaniesHouseRepository()
			val companiesNavigator = viewModelContext.activity<CompaniesActivity>().injectCompaniesNavigator()
			val errorResolver = viewModelContext.activity<CompaniesActivity>().injectErrorResolver()
			val recentSearchesString = viewModelContext.activity<CompaniesActivity>().injectRecentSearchesString()
			return CompaniesViewModel(
					state,
					companiesRepository,
					companiesNavigator,
					errorResolver,
					recentSearchesString
			)
		}
	}

	//region Recent searches

	fun showRecentSearches() {
		companiesRepository.recentSearches()
				.execute {
					copy(
							searchHistoryRequest = it.resolveErrorOrProceed(errorResolver),
							searchHistoryVisitables = convertSearchHistoryToVisitables(it() ?: emptyList())
					)
				}
	}

	private fun convertSearchHistoryToVisitables(reply: List<SearchHistoryItem>):
			List<AbstractSearchHistoryVisitable> {
		val searchHistoryVisitables: MutableList<AbstractSearchHistoryVisitable> =
				reply.map { item ->
					SearchHistoryVisitable(SearchHistoryItem(
							item.companyName,
							item.companyNumber,
							System.currentTimeMillis()
					))
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
		if (queryText.length > 2) {
			search(queryText)
		} else {
			setState {
				copy(
						queryText = queryText,
						searchRequest = Uninitialized,
						searchVisitables = emptyList(),
						filteredSearchVisitables = emptyList()
				)
			}
		}
	}

	fun search(queryText: String) {
		companiesRepository.searchCompanies(queryText, "0")
				.execute {
					copy(
							queryText = queryText,
							totalCount = it()?.totalResults ?: 0,
							searchRequest = it.resolveErrorOrProceed(errorResolver),
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
									queryText = queryText,
									totalCount = it()?.totalResults ?: 0,
									searchRequest = it.resolveErrorOrProceed(errorResolver),
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

	private fun convertSearchResultsToVisitables(reply: CompanySearchResult): List<AbstractSearchVisitable> {
		return ArrayList(reply.items.map { item -> SearchVisitable(item) })
	}

	private fun convertLoadMoreSearchResultsToVisitables(
			currentSearchVisitables: List<AbstractSearchVisitable>,
			reply: CompanySearchResult
	): List<AbstractSearchVisitable> {
		val updatedList = currentSearchVisitables.toMutableList()
		updatedList.addAll(reply.items.map { item -> SearchVisitable(item) })
		return updatedList
	}

	private fun filterSearchResults(
			filterState: FilterState?,
			searchVisitables: List<AbstractSearchVisitable>)
			: Single<List<AbstractSearchVisitable>> {
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
		val searchHistoryItems = companiesRepository.addRecentSearchItem(SearchHistoryItem(name, number, System.currentTimeMillis()))
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
							isFavorite = companiesRepository.isFavourite(SearchHistoryItem(
									this.companyName,
									this.companyNumber,
									0
							)),
							companyRequest = it,
							company = it() ?: Company(),
							addressString = getAddressString(it()),
							natureOfBusinessString = if (it()?.sicCodes?.isNotEmpty() == true) {
								it()?.let { company ->
									"${company.sicCodes[0]} ${companiesRepository.sicLookup(company.sicCodes[0])}"
								} ?: "No data"
							} else {
								//TODO Create a string provider to get this from strings.xml, but don't rely on context here
								"No data"
							}
					)
				}
	}

	private fun getAddressString(company: Company?): String {
		return company?.registeredOfficeAddress?.addressLine2?.let { line2 ->
			line2
		} ?: run {
			(company?.registeredOfficeAddress?.addressLine1
					+ ", "
					+ company?.registeredOfficeAddress?.locality
					+ ", "
					+ company?.registeredOfficeAddress?.postalCode)
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
							isFavorite = companiesRepository.isFavourite(SearchHistoryItem(
									this.companyName,
									this.companyNumber,
									0
							))
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
		return ArrayList(favourites.map { item -> FavouritesVisitable(FavouritesItem(item)) })
	}

	fun removeFavourite(favouriteToRemove: SearchHistoryItem) {
		companiesRepository.removeFavourite(favouriteToRemove)
		loadFavourites()
	}

	//endregion

}
