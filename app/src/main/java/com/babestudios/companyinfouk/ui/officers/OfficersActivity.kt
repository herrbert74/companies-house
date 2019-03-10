package com.babestudios.companyinfouk.ui.officers

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import com.babestudios.companyinfouk.data.model.officers.Officers
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.pascalwelsch.compositeandroid.activity.CompositeActivity

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.companyinfouk.ui.officerdetails.createOfficerDetailsIntent

class OfficersActivity : CompositeActivity(), OfficersActivityView, OfficersAdapter.OfficersRecyclerViewClickListener {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.officers_recycler_view)
	internal var officersRecyclerView: RecyclerView? = null

	private var officersAdapter: OfficersAdapter? = null

	@JvmField
	@BindView(R.id.progressbar)
	internal var progressbar: ProgressBar? = null

	@Inject
	internal lateinit var officersPresenter: OfficersPresenter

	override lateinit var companyNumber: String


	internal var officersActivityPlugin = TiActivityPlugin<OfficersPresenter, OfficersActivityView> {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		officersPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(officersActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_officers)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar!!.setDisplayHomeAsUpEnabled(true)
			supportActionBar!!.setTitle(R.string.officers)
			toolbar!!.setNavigationOnClickListener { onBackPressed() }
		}
		companyNumber = intent.getStringExtra("companyNumber")
		createOfficersRecyclerView()


	}

	private fun createOfficersRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		officersRecyclerView!!.layoutManager = linearLayoutManager
		officersRecyclerView!!.addItemDecoration(
				DividerItemDecoration(this))

		officersRecyclerView!!.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				officersActivityPlugin.presenter.loadMoreOfficers(page)
			}
		})
	}

	override fun showProgress() {
		progressbar!!.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar!!.visibility = View.GONE
	}


	override fun showOfficers(officers: Officers) {
		if (officersRecyclerView!!.adapter == null) {
			officersAdapter = OfficersAdapter(this@OfficersActivity, officers)
			officersRecyclerView!!.adapter = officersAdapter
		} else {
			officersAdapter!!.updateItems(officers)
		}
	}

	override fun officersItemClicked(v: View, position: Int, officerItem: OfficerItem) {
		baseActivityPlugin.startActivityWithRightSlide(createOfficerDetailsIntent(officerItem))
	}

	override fun showError() {
		Toast.makeText(this, R.string.could_not_retrieve_officer_info, Toast.LENGTH_LONG).show()
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
