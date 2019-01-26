package com.babestudios.companyinfouk.ui.charges

import android.os.Bundle
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
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.data.model.charges.ChargesItem
import com.babestudios.companyinfouk.ui.chargesdetails.createChargesDetailsIntent
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.companyinfouk.utils.DividerItemDecoration
import com.babestudios.companyinfouk.utils.EndlessRecyclerViewScrollListener
import com.pascalwelsch.compositeandroid.activity.CompositeActivity
import kotlinx.android.synthetic.main.activity_charges.*
import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin
import javax.inject.Inject
import javax.inject.Singleton

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
		companyNumber = intent.getStringExtra("companyNumber")
		createChargesRecyclerView()
		setSupportActionBar(pabCharges.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setTitle(R.string.charges)
		pabCharges.setNavigationOnClickListener { onBackPressed() }
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
		baseActivityPlugin.startActivityWithRightSlide(this.createChargesDetailsIntent(chargesItem))
	}

	override fun showNoCharges() {
		lblNoCharges!!.visibility = View.VISIBLE
		chargesRecyclerView!!.visibility = View.GONE
	}

}
