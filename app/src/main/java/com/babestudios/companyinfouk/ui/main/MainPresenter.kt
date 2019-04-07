package com.babestudios.companyinfouk.ui.main

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.ObserverWrapper
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
import com.uber.autodispose.AutoDispose
import io.reactivex.CompletableSource
import javax.inject.Inject

interface Search2PresenterContract : Presenter<SearchState, MainViewModel> {
	fun search(queryText: String)
	fun loadMoreSearch(page: Int)
	fun onSearchQueryChanged(queryText: String)
	fun searchItemClicked(number: String, name: String)
	fun fabMainClicked()
	fun clearAllRecentSearches()
}

@SuppressLint("CheckResult")
class Search2Presenter
@Inject
constructor(var companiesRepository: CompaniesRepository) : BasePresenter<SearchState, MainViewModel>(), Search2PresenterContract {

	override fun setViewModel(viewModel: MainViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		viewModel.state.value?.searchItems?.let {
			sendToViewModel {
				it.apply {
					this.isLoading = false
					this.contentChange = ContentChange.SEARCH_ITEMS_RECEIVED
				}
			}
		} ?: run {
			showRecentSearches()
		}
	}

	private fun showRecentSearches() {
		val searchHistoryItems = java.util.ArrayList(companiesRepository.recentSearches)
		sendToViewModel {
			it.apply {
				this.isLoading = false
				this.contentChange = ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED
				this.searchHistoryItems = convertSearchHistoryToVisitables(searchHistoryItems)
			}
		}
	}

	override fun search(queryText: String) {
		companiesRepository.searchCompanies(queryText, "0")
				.`as`(AutoDispose.autoDisposable(lifeCycleCompletable))
				.subscribeWith(object : ObserverWrapper<CompanySearchResult>(this) {
					override fun onSuccess(reply: CompanySearchResult) {
						sendToViewModel {
							it.apply {
								this.isLoading = false
								this.isSearchLoading = false
								this.queryText = queryText
								this.contentChange = ContentChange.SEARCH_ITEMS_RECEIVED
								this.searchItems = convertSearchResultsToVisitables(reply)
								this.totalCount = reply.totalResults
							}
						}
					}
				})
	}


	override fun loadMoreSearch(page: Int) {
		if (viewModel.state.value?.searchItems == null || viewModel.state.value?.searchItems!!.size < viewModel.state.value?.totalCount!!) {
			companiesRepository.searchCompanies(viewModel.state.value?.queryText
					?: "", (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString())
					.subscribeWith(object : ObserverWrapper<CompanySearchResult>(this) {
						override fun onSuccess(reply: CompanySearchResult) {
							val newList = viewModel.state.value?.searchItems?.toMutableList()
							newList?.addAll(convertSearchResultsToVisitables(reply))
							sendToViewModel {
								it.apply {
									this.isLoading = false
									this.contentChange = ContentChange.SEARCH_ITEMS_RECEIVED
									newList?.toList()?.let { list -> this.searchItems = list }
								}
							}
						}
					})
		}
	}

	private fun convertSearchHistoryToVisitables(reply: List<SearchHistoryItem>): List<AbstractSearchHistoryVisitable> {
		val searchHistoryVisitables: MutableList<AbstractSearchHistoryVisitable> = reply.map { item -> SearchHistoryVisitable(SearchHistoryItem(item.companyName, item.companyNumber, System.currentTimeMillis())) }.toMutableList()
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
		}
		search(queryText)
	}

	override fun searchItemClicked(number: String, name: String) {
		val searchHistoryItems = companiesRepository.addRecentSearchItem(SearchHistoryItem(name, number, System.currentTimeMillis()))
		sendToViewModel {
			it.apply {
				this.searchHistoryItems = convertSearchHistoryToVisitables(searchHistoryItems)
				this.contentChange = ContentChange.SEARCH_HISTORY_ITEMS_UPDATED
			}
		}
	}

	//endregion

	override fun fabMainClicked() {
		if (viewModel.state.value.contentChange == ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED
				/*|| viewModel.state.value.contentChange == ContentChange.SEARCH_HISTORY_ITEMS_UPDATED*/) {
			sendToViewModel {
				it.apply {
					this.contentChange = ContentChange.DELETE_SEARCH_HISTORY
				}
			}
		} /*else if (viewModel.state.value.contentChange == ContentChange.SEARCH_ITEMS_RECEIVED) {
			searchActivityView?.clearSearchView()
			searchHistoryItems?.let { searchActivityView?.refreshRecentSearchesAdapter(it) }
			showRecentSearches()
		}*/
	}

	override fun clearAllRecentSearches() {
		companiesRepository.clearAllRecentSearches()
		sendToViewModel {
			it.apply {
				this.contentChange = ContentChange.SEARCH_HISTORY_ITEMS_RECEIVED
				this.searchHistoryItems = convertSearchHistoryToVisitables(ArrayList())
			}
		}
	}
}