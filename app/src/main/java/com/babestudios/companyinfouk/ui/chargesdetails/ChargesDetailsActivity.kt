package com.babestudios.companyinfouk.ui.chargesdetails

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.charges.ChargesItem
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.companyinfouk.utils.DividerItemDecorationWithSubHeading
import com.google.gson.Gson
import com.pascalwelsch.compositeandroid.activity.CompositeActivity

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife

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
		val chargesItemString = intent.getStringExtra("chargesItem")
		val gson = Gson()
		val chargesItem = gson.fromJson(chargesItemString, ChargesItem::class.java)

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
