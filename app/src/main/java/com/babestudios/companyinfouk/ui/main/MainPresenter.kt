package com.babestudios.companyinfouk.ui.main

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.SingleObserverWrapper
import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.ui.main.recents.AbstractSearchHistoryVisitable
import com.babestudios.companyinfouk.ui.main.recents.SearchHistoryHeaderItem
import com.babestudios.companyinfouk.ui.main.recents.SearchHistoryHeaderVisitable
import com.babestudios.companyinfouk.ui.main.recents.SearchHistoryVisitable
import com.babestudios.companyinfouk.ui.main.search.AbstractSearchVisitable
import com.babestudios.companyinfouk.ui.main.search.SearchVisitable
import com.babestudios.companyinfouk.ui.search.SearchPresenter
import com.uber.autodispose.AutoDispose
import io.reactivex.CompletableSource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface Search2PresenterContract : Presenter<SearchState, MainViewModel> {
	fun search(queryText: String)
	fun loadMoreSearch(page: Int)
	fun onSearchQueryChanged(queryText: String)
	fun searchItemClicked(number: String, name: String)
	fun fabMainClicked()
	fun clearAllRecentSearches()
	fun setFilterState(filterState: FilterState)
}

@SuppressLint("CheckResult")
class Search2Presenter
@Inject
constructor(var companiesRepository: CompaniesRepository) : BasePresenter<SearchState, MainViewModel>(), Search2PresenterContract {

	override fun setViewModel(viewModel: MainViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		if (viewModel.state.value?.searchVisitables?.isNotEmpty() == true) {
			showRecentSearches()
			sendToViewModel {
				it.apply {
					this.isLoading = false
					this.contentChange = ContentChange.SEARCH_ITEMS_RECEIVED_FROM_SAVED_INSTANCE_STATE
				}
			}
		} else {
			showRecentSearches()
		}
	}

	private fun showRecentSearches() {
		val searchHistoryItems = java.util.ArrayList(companiesRepository.recentSearches)
		sendToViewModel {
			it.apply {
				this.isLoading = false
				this.contentChange = ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED
				this.searchHistoryVisitables = convertSearchHistoryToVisitables(searchHistoryItems)
			}
		}
	}

	override fun search(queryText: String) {
		companiesRepository.searchCompanies(queryText, "0")
				.`as`(AutoDispose.autoDisposable(lifeCycleCompletable))
				.subscribeWith(object : SingleObserverWrapper<CompanySearchResult>(this) {
					override fun onSuccess(reply: CompanySearchResult) {
						val searchVisitables = convertSearchResultsToVisitables(reply)
						filterSearchResults(viewModel.state.value.filterState, searchVisitables)
								.subscribe { filteredSearchResults ->
									sendToViewModel {
										it.apply {
											this.isLoading = false
											this.isSearchLoading = false
											this.queryText = queryText
											this.contentChange = ContentChange.SEARCH_ITEMS_RECEIVED
											this.searchVisitables = searchVisitables
											this.filteredSearchVisitables = filteredSearchResults
											this.totalCount = reply.totalResults
										}
									}
								}
					}
				})
	}


	override fun loadMoreSearch(page: Int) {
		if (viewModel.state.value?.searchVisitables == null || viewModel.state.value?.searchVisitables!!.size < viewModel.state.value?.totalCount!!) {
			companiesRepository.searchCompanies(viewModel.state.value?.queryText
					?: "", (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString())
					.subscribeWith(object : SingleObserverWrapper<CompanySearchResult>(this) {
						override fun onSuccess(reply: CompanySearchResult) {
							val newSearchVisitables = viewModel.state.value.searchVisitables.toMutableList()
							newSearchVisitables.addAll(convertSearchResultsToVisitables(reply))
							filterSearchResults(viewModel.state.value.filterState, newSearchVisitables.toList())
									.subscribe { filteredSearchResults ->
										sendToViewModel {
											it.apply {
												this.isLoading = false
												this.isSearchLoading = false
												this.contentChange = ContentChange.SEARCH_ITEMS_RECEIVED
												this.searchVisitables = newSearchVisitables
												this.filteredSearchVisitables = filteredSearchResults
											}
										}
									}
						}
					})
		}
	}

	private fun convertSearchHistoryToVisitables(reply: List<SearchHistoryItem>): List<AbstractSearchHistoryVisitable> {
		val searchHistoryVisitables: MutableList<AbstractSearchHistoryVisitable> = reply.map { item -> SearchHistoryVisitable(SearchHistoryItem(item.companyName, item.companyNumber, System.currentTimeMillis())) }.toMutableList()
		if (searchHistoryVisitables.size > 0)
			searchHistoryVisitables.add(0, SearchHistoryHeaderVisitable(SearchHistoryHeaderItem(CompaniesHouseApplication.context.getString(R.string.recent_searches))))
		return searchHistoryVisitables
	}

	private fun convertSearchResultsToVisitables(reply: CompanySearchResult): List<AbstractSearchVisitable> {
		return ArrayList(reply.items.map { item -> SearchVisitable(item) })
	}

	//region search

	override fun onSearchQueryChanged(queryText: String) {
		if (queryText.length > 2) {
			sendToViewModel {
				it.apply {
					this.isSearchLoading = true
				}
			}
			search(queryText)
		} else {
			sendToViewModel {
				it.apply {
					this.isSearchLoading = false
					this.contentChange = ContentChange.SEARCH_ITEMS_RECEIVED
					this.searchVisitables = ArrayList()
					this.filteredSearchVisitables = ArrayList()
					this.queryText = queryText
				}
			}
		}
	}

	override fun searchItemClicked(number: String, name: String) {
		val searchHistoryItems = companiesRepository.addRecentSearchItem(SearchHistoryItem(name, number, System.currentTimeMillis()))
		sendToViewModel {
			it.apply {
				this.searchHistoryVisitables = convertSearchHistoryToVisitables(searchHistoryItems)
				this.contentChange = ContentChange.SEARCH_HISTORY_ITEMS_UPDATED
			}
		}
	}

	//endregion

	override fun fabMainClicked() {
		if (viewModel.state.value.contentChange == ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED) {
			sendToViewModel {
				it.apply {
					this.contentChange = ContentChange.DELETE_SEARCH_HISTORY
				}
			}
		}
	}

	override fun clearAllRecentSearches() {
		companiesRepository.clearAllRecentSearches()
		sendToViewModel {
			it.apply {
				this.contentChange = ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED
				this.searchHistoryVisitables = convertSearchHistoryToVisitables(ArrayList())
			}
		}
	}

	override fun setFilterState(filterState: FilterState) {
		if (filterState.ordinal > SearchPresenter.FilterState.FILTER_SHOW_ALL.ordinal) {
			filterSearchResults(filterState, viewModel.state.value.searchVisitables)
					.subscribe { result ->
						sendToViewModel {
							it.apply {
								this.contentChange = ContentChange.SEARCH_ITEMS_RECEIVED
								this.filteredSearchVisitables = result
								this.filterState = filterState
							}
						}
					}
		} else {
			sendToViewModel {
				it.apply {
					this.contentChange = ContentChange.SEARCH_ITEMS_RECEIVED
					this.filteredSearchVisitables = searchVisitables.toList()
					this.filterState = filterState
				}
			}
		}
	}

	private fun filterSearchResults(filterState: FilterState, searchVisitables: List<AbstractSearchVisitable>): Single<List<AbstractSearchVisitable>> {
		return Observable.fromIterable(searchVisitables)
				.filter { companySearchResultItem ->
					val searchItem = (companySearchResultItem as SearchVisitable).searchItem
					filterState == FilterState.FILTER_SHOW_ALL || (searchItem.companyStatus != null && searchItem.companyStatus.equals(filterState.toString(), ignoreCase = true))
				}
				.observeOn(Schedulers.trampoline())
				.toList()
	}
}