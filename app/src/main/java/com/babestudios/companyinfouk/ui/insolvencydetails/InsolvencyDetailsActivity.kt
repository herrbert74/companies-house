package com.babestudios.companyinfouk.ui.insolvencydetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.google.gson.Gson
import com.pascalwelsch.compositeandroid.activity.CompositeActivity
import java.util.*

private const val INSOLVENCY_CASE = "com.babestudios.companyinfouk.ui.insolvency_case"

class InsolvencyDetailsActivity : CompositeActivity() {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.insolvency_details_recycler_view)
	internal var insolvencyDetailsRecyclerView: RecyclerView? = null


	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_insolvency_details)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		val insolvencyCase = intent.getParcelableExtra<InsolvencyCase>(INSOLVENCY_CASE)

		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar!!.setDisplayHomeAsUpEnabled(true)
			supportActionBar!!.setTitle(R.string.insolvency_details)
			toolbar!!.setNavigationOnClickListener { onBackPressed() }
		}
		createInsolvencyDetailsRecyclerView(insolvencyCase)
	}

	private fun createInsolvencyDetailsRecyclerView(insolvencyCase: InsolvencyCase) {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		insolvencyDetailsRecyclerView!!.layoutManager = linearLayoutManager
		val titlePositions = ArrayList<Int>()
		titlePositions.add(0)
		titlePositions.add(insolvencyCase.dates.size + 1)
		insolvencyDetailsRecyclerView!!.addItemDecoration(
				DividerItemDecorationWithSubHeading(this, titlePositions))
		val adapter = InsolvencyDetailsAdapter(insolvencyCase.dates, insolvencyCase.practitioners)
		insolvencyDetailsRecyclerView!!.adapter = adapter
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}

fun Context.createInsolvencyDetailsIntent(insolvencyCase: InsolvencyCase): Intent {
	return Intent(this, InsolvencyDetailsActivity::class.java)
			.putExtra(INSOLVENCY_CASE, insolvencyCase)
}