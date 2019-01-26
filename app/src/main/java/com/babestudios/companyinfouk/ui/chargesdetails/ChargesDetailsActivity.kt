package com.babestudios.companyinfouk.ui.chargesdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.charges.ChargesItem
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.companyinfouk.utils.DividerItemDecorationWithSubHeading
import com.pascalwelsch.compositeandroid.activity.CompositeActivity
import java.util.*

private const val CHARGES_ITEM = "com.babestudios.companyinfouk.ui.charges_item"

class ChargesDetailsActivity : CompositeActivity() {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.charges_details_recycler_view)
	internal var chargesDetailsRecyclerView: RecyclerView? = null

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_charges_details)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		val chargesItem = intent.getParcelableExtra<ChargesItem>(CHARGES_ITEM)

		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar!!.setDisplayHomeAsUpEnabled(true)
			supportActionBar!!.setTitle(R.string.charges_details)
			toolbar!!.setNavigationOnClickListener { onBackPressed() }
		}
		createChargesDetailsRecyclerView(chargesItem)
	}

	private fun createChargesDetailsRecyclerView(chargesItem: ChargesItem) {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		chargesDetailsRecyclerView!!.layoutManager = linearLayoutManager
		val titlePositions = ArrayList<Int>()
		titlePositions.add(0)
		chargesDetailsRecyclerView!!.addItemDecoration(
				DividerItemDecorationWithSubHeading(this, titlePositions))
		val adapter = ChargesDetailsAdapter(this, chargesItem)
		chargesDetailsRecyclerView!!.adapter = adapter
	}

}

fun Context.createChargesDetailsIntent(chargesItem: ChargesItem): Intent {
	return Intent(this, ChargesDetailsActivity::class.java)
			.putExtra(CHARGES_ITEM, chargesItem)
}

