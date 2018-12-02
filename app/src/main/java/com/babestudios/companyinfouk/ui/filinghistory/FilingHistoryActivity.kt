package com.babestudios.companyinfouk.ui.filinghistory

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList
import com.babestudios.companyinfouk.ui.filinghistorydetails.FilingHistoryDetailsActivity
import com.babestudios.companyinfouk.ui.search.SearchFilterAdapter
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.companyinfouk.utils.DividerItemDecoration
import com.babestudios.companyinfouk.utils.EndlessRecyclerViewScrollListener
import com.google.gson.Gson
import com.pascalwelsch.compositeandroid.activity.CompositeActivity
import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin
import javax.inject.Inject

class FilingHistoryActivity : CompositeActivity(), FilingHistoryActivityView, FilingHistoryAdapter.FilingHistoryRecyclerViewClickListener {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.filing_history_recycler_view)
	internal var filingHistoryRecyclerView: RecyclerView? = null

	@JvmField
	@BindView(R.id.progressbar)
	internal var progressbar: ProgressBar? = null

	@JvmField
	@BindView(R.id.collapsing_toolbar)
	internal var collapsingToolbarLayout: CollapsingToolbarLayout? = null

	private var filingHistoryAdapter: FilingHistoryAdapter? = null

	@Inject
	lateinit var filingHistoryPresenter: FilingHistoryPresenter

	override lateinit var companyNumber: String
	private var initialCategoryFilter: FilingHistoryPresenter.CategoryFilter? = null

	internal var filingHistoryActivityPlugin = TiActivityPlugin<FilingHistoryPresenter, FilingHistoryActivityView> {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		filingHistoryPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	override val filingCategory: String? = null

	init {

		addPlugin(filingHistoryActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_filing_history)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		setSupportActionBar(toolbar)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
		toolbar?.setNavigationOnClickListener { onBackPressed() }
		companyNumber = intent.getStringExtra("companyNumber")
		createFilingHistoryRecyclerView()

		collapsingToolbarLayout!!.title = getString(R.string.filing_history)
	}

	private fun createFilingHistoryRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		filingHistoryRecyclerView!!.layoutManager = linearLayoutManager
		filingHistoryRecyclerView!!.addItemDecoration(
				DividerItemDecoration(this))

		filingHistoryRecyclerView!!.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				filingHistoryActivityPlugin.presenter.loadMoreFilingHistory(page)
			}
		})
	}

	override fun showProgress() {
		progressbar!!.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar!!.visibility = View.GONE
	}

	override fun showError() {
		Toast.makeText(this, R.string.could_not_retrieve_filing_history_info, Toast.LENGTH_LONG).show()
	}


	override fun showFilingHistory(filingHistoryList: FilingHistoryList, categoryFilter: FilingHistoryPresenter.CategoryFilter) {
		if (filingHistoryRecyclerView!!.adapter == null) {
			filingHistoryAdapter = FilingHistoryAdapter(this@FilingHistoryActivity, filingHistoryList, categoryFilter)
			filingHistoryRecyclerView!!.adapter = filingHistoryAdapter
		} else {
			filingHistoryAdapter!!.updateItems(filingHistoryList)
		}
	}

	override fun filingItemClicked(v: View, position: Int, item: FilingHistoryItem) {
		val gson = Gson()
		val jsonItem = gson.toJson(item, FilingHistoryItem::class.java)
		val intent = Intent(this, FilingHistoryDetailsActivity::class.java)
		intent.putExtra("filingHistoryItem", jsonItem)
		baseActivityPlugin.startActivityWithRightSlide(intent)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.filing_history_menu, menu)

		val item = menu.findItem(R.id.spinner)
		val spinner = MenuItemCompat.getActionView(item) as Spinner
		spinner.setBackgroundResource(0)
		spinner.setPadding(0, 0, resources.getDimensionPixelOffset(R.dimen.view_margin), 0)
		val adapter = SearchFilterAdapter(this@FilingHistoryActivity, resources.getStringArray(R.array.filing_history_categories), true)
		spinner.adapter = adapter
		if (initialCategoryFilter != null) {
			spinner.setSelection(initialCategoryFilter!!.ordinal)
			initialCategoryFilter = null
		}
		spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
				filingHistoryActivityPlugin.presenter.setCategoryFilter(position)
			}

			override fun onNothingSelected(parent: AdapterView<*>) {

			}
		}
		return true
	}

	override fun setInitialCategoryFilter(categoryFilter: FilingHistoryPresenter.CategoryFilter) {
		this.initialCategoryFilter = categoryFilter
	}

	override fun setFilterOnAdapter(categoryFilter: FilingHistoryPresenter.CategoryFilter) {
		if (filingHistoryAdapter != null) {
			filingHistoryAdapter!!.setFilterOnAdapter(categoryFilter)
		}
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
