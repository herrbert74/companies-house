package com.babestudios.companyinfouk.ui.insolvency

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.ui.insolvencydetails.InsolvencyDetailsActivity
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.companyinfouk.utils.DividerItemDecoration
import com.google.gson.Gson
import com.pascalwelsch.compositeandroid.activity.CompositeActivity
import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin
import javax.inject.Inject

class InsolvencyActivity : CompositeActivity(), InsolvencyActivityView, InsolvencyAdapter.InsolvencyRecyclerViewClickListener {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.insolvency_recycler_view)
	internal var insolvencyRecyclerView: RecyclerView? = null

	@JvmField
	@BindView(R.id.lblNoInsolvency)
	internal var lblNoInsolvency: TextView? = null

	private var insolvencyAdapter: InsolvencyAdapter? = null

	@JvmField
	@BindView(R.id.progressbar)
	internal var progressbar: ProgressBar? = null

	@JvmField
	@BindView(R.id.collapsing_toolbar)
	internal var collapsingToolbarLayout: CollapsingToolbarLayout? = null

	@Inject
	lateinit var insolvencyPresenter: InsolvencyPresenter

	override lateinit var companyNumber: String

	private var insolvencyActivityPlugin = TiActivityPlugin<InsolvencyPresenter, InsolvencyActivityView> {
		CompaniesHouseApplication.getInstance().applicationComponent.inject(this)
		insolvencyPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {

		addPlugin(insolvencyActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_insolvency)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		toolbar?.also {
			setSupportActionBar(it)
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
			it.setNavigationOnClickListener { onBackPressed() }
		}
		companyNumber = intent.getStringExtra("companyNumber")
		createInsolvencyRecyclerView()

		collapsingToolbarLayout?.title = getString(R.string.insolvency)

	}

	private fun createInsolvencyRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		insolvencyRecyclerView?.layoutManager = linearLayoutManager
		insolvencyRecyclerView?.addItemDecoration(
				DividerItemDecoration(this))
	}

	override fun showProgress() {
		progressbar?.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar?.visibility = View.GONE
	}


	override fun showInsolvency(insolvency: Insolvency) {
		if (insolvencyRecyclerView?.adapter == null) {
			insolvencyAdapter = InsolvencyAdapter(this@InsolvencyActivity, insolvency)
			insolvencyRecyclerView?.adapter = insolvencyAdapter
		} else {
			insolvencyAdapter?.updateItems(insolvency)
		}
	}

	override fun insolvencyItemClicked(v: View, position: Int, insolvencyCase: InsolvencyCase) {
		val gson = Gson()
		val jsonItem = gson.toJson(insolvencyCase, InsolvencyCase::class.java)
		val intent = Intent(this, InsolvencyDetailsActivity::class.java)
		intent.putExtra("insolvencyCase", jsonItem)
		baseActivityPlugin.startActivityWithRightSlide(intent)
	}

	override fun showNoInsolvency() {
		lblNoInsolvency?.visibility = View.VISIBLE
		insolvencyRecyclerView?.visibility = View.GONE
	}

	override fun showError() {
		Toast.makeText(this, R.string.could_not_retrieve_insolvency_info, Toast.LENGTH_LONG).show()
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
