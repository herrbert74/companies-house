package com.babestudios.companyinfouk.ui.charges

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.data.model.charges.ChargesItem
import com.babestudios.companyinfouk.ui.chargesdetails.ChargesDetailsActivity
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.companyinfouk.utils.DividerItemDecoration
import com.babestudios.companyinfouk.utils.EndlessRecyclerViewScrollListener
import com.google.gson.Gson
import com.pascalwelsch.compositeandroid.activity.CompositeActivity

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin

import javax.inject.Inject
import javax.inject.Singleton

import butterknife.BindView
import butterknife.ButterKnife

class ChargesActivity : CompositeActivity(), ChargesActivityView, ChargesAdapter.ChargesRecyclerViewClickListener {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.charges_recycler_view)
	internal var chargesRecyclerView: RecyclerView? = null

	@JvmField
	@BindView(R.id.lblNoCharges)
	internal var lblNoCharges: TextView? = null

	private var chargesAdapter: ChargesAdapter? = null

	@JvmField
	@BindView(R.id.progressbar)
	internal var progressbar: ProgressBar? = null

	@Singleton
	@Inject
	internal lateinit var companiesRepository: CompaniesRepository

	override lateinit var companyNumber: String

	@Inject
	lateinit var chargesPresenter: ChargesPresenter

	internal var chargesActivityPlugin = TiActivityPlugin<ChargesPresenter, ChargesActivityView> {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		chargesPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(chargesActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {

		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_charges)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar!!.setDisplayHomeAsUpEnabled(true)
			supportActionBar!!.setTitle(R.string.charges)
			toolbar!!.setNavigationOnClickListener { onBackPressed() }
		}
		companyNumber = intent.getStringExtra("companyNumber")
		createChargesRecyclerView()


	}

	private fun createChargesRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		chargesRecyclerView!!.layoutManager = linearLayoutManager
		chargesRecyclerView!!.addItemDecoration(
				DividerItemDecoration(this))

		chargesRecyclerView!!.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				chargesActivityPlugin.presenter.loadMoreCharges(page)
			}
		})
	}

	override fun showProgress() {
		progressbar!!.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar!!.visibility = View.GONE
	}


	override fun showCharges(charges: Charges) {
		if (chargesRecyclerView!!.adapter == null) {
			chargesAdapter = ChargesAdapter(this@ChargesActivity, charges, companiesRepository)
			chargesRecyclerView!!.adapter = chargesAdapter
		} else {
			chargesAdapter!!.updateItems(charges)
		}
	}

	override fun showError() {
		Toast.makeText(this, R.string.could_not_retrieve_charges_info, Toast.LENGTH_LONG).show()
	}

	override fun chargesItemClicked(v: View, position: Int, chargesItem: ChargesItem) {
		val gson = Gson()
		val jsonItem = gson.toJson(chargesItem, ChargesItem::class.java)
		val intent = Intent(this, ChargesDetailsActivity::class.java)
		intent.putExtra("chargesItem", jsonItem)
		baseActivityPlugin.startActivityWithRightSlide(intent)
	}

	override fun showNoCharges() {
		lblNoCharges!!.visibility = View.VISIBLE
		chargesRecyclerView!!.visibility = View.GONE
	}

}
