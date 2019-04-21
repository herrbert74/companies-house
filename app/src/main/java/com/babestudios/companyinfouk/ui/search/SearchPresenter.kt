package com.babestudios.companyinfouk.ui.search

import android.util.Log
import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import net.grandcentrix.thirtyinch.TiPresenter
import java.util.*
import javax.inject.Inject


class SearchPresenter @Inject
constructor(internal var companiesRepository: CompaniesRepository) : TiPresenter<SearchActivityView>(), SingleObserver<CompanySearchResult> {

	private var showState = ShowState.RECENT_SEARCHES

	private var queryText: String? = null

	private var searchHistoryItems: ArrayList<SearchHistoryItem>? = null
	private var companySearchResult: CompanySearchResult? = null

	private var filterState = FilterState.FILTER_SHOW_ALL

	private var searchActivityView: SearchActivityView? = null

	enum class FabImage {
		FAB_IMAGE_RECENT_SEARCH_DELETE,
		FAB_IMAGE_SEARCH_CLOSE
	}

	private enum class ShowState {
		RECENT_SEARCHES,
		SEARCH
	}

	enum class FilterState(private val name2: String) {
		FILTER_SHOW_ALL("all"),
		FILTER_ACTIVE("active"),
		FILTER_LIQUIDATION("liquidation"),
		FILTER_OPEN("open"),
		FILTER_DISSOLVED("dissolved");

		override fun toString(): String {
			return name2
		}
	}

	override fun onAttachView(view: SearchActivityView) {
		super.onAttachView(view)
		searchActivityView = view
		if (showState == ShowState.RECENT_SEARCHES) {
			showRecentSearches()
		} else {
			view.clearSearchView()
			companySearchResult?.let {
				view.showCompanySearchResult(it, false, filterState)
				view.setInitialFilterState(filterState)
				view.changeFabImage(FabImage.FAB_IMAGE_SEARCH_CLOSE)
			}
		}
	}

	private fun showRecentSearches() {
		searchHistoryItems = ArrayList(companiesRepository.recentSearches)
		searchHistoryItems?.let { searchActivityView?.showRecentSearches(it) }
		searchActivityView?.changeFabImage(FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE)
		showState = ShowState.RECENT_SEARCHES
	}

	override fun onError(e: Throwable) {
		Log.d("test", "onError: " + e.fillInStackTrace())
		if (searchActivityView != null) {
			searchActivityView?.showError()
			searchActivityView?.hideProgress()
		}
	}

	override fun onSubscribe(d: Disposable) {

	}

	override fun onSuccess(companySearchResult: CompanySearchResult) {
		showState = ShowState.SEARCH
		this.companySearchResult = companySearchResult
		searchActivityView?.hideProgress()
		searchActivityView?.showCompanySearchResult(companySearchResult, true, filterState)
		searchActivityView?.changeFabImage(FabImage.FAB_IMAGE_SEARCH_CLOSE)
	}

	internal fun search(queryText: String) {
		this.queryText = queryText
		searchActivityView?.showProgress()
		companiesRepository.searchCompanies(queryText, "0").subscribe(this)
	}

	internal fun searchLoadMore(page: Int) {
		queryText?.let {
			companiesRepository.searchCompanies(it, (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString())
					.subscribe(this)
		}
	}

	internal fun getCompany(companyName: String, companyNumber: String) {
		searchActivityView?.startCompanyActivity(companyNumber, companyName)
		searchHistoryItems = companiesRepository.addRecentSearchItem(SearchHistoryItem(companyName, companyNumber, System.currentTimeMillis()))
	}

	internal fun onFabClicked() {
		if (showState == ShowState.RECENT_SEARCHES) {
			searchActivityView?.showDeleteRecentSearchesDialog()
		} else if (showState == ShowState.SEARCH) {
			searchActivityView?.clearSearchView()
			searchHistoryItems?.let { searchActivityView?.refreshRecentSearchesAdapter(it) }
			showRecentSearches()
		}
	}

	internal fun clearAllRecentSearches() {
		companiesRepository.clearAllRecentSearches()
		searchActivityView?.refreshRecentSearchesAdapter(ArrayList())
	}

	internal fun setFilterState(state: Int) {
		filterState = FilterState.values()[state]
		if (searchActivityView != null && showState == ShowState.SEARCH) {
			searchActivityView?.setFilterOnAdapter(filterState)
		}
	}
}
