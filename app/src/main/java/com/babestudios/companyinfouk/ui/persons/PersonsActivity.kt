package com.babestudios.companyinfouk.ui.persons

import android.content.Intent
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
import com.babestudios.companyinfouk.data.model.persons.Person
import com.babestudios.companyinfouk.data.model.persons.Persons
import com.babestudios.companyinfouk.ui.personsdetails.PersonsDetailsActivity
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.companyinfouk.utils.DividerItemDecoration
import com.google.gson.Gson
import com.pascalwelsch.compositeandroid.activity.CompositeActivity
import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin
import javax.inject.Inject

class PersonsActivity : CompositeActivity(), PersonsActivityView, PersonsAdapter.PersonsRecyclerViewClickListener {

	@JvmField
	@BindView(R.id.toolbar)
	internal var toolbar: Toolbar? = null

	@JvmField
	@BindView(R.id.persons_recycler_view)
	internal var personsRecyclerView: RecyclerView? = null

	@JvmField
	@BindView(R.id.lblNoPersons)
	internal var lblNoPersons: TextView? = null

	private var personsAdapter: PersonsAdapter? = null

	@JvmField
	@BindView(R.id.progressbar)
	internal var progressbar: ProgressBar? = null

	@Inject
	internal lateinit var personsPresenter: PersonsPresenter

	override lateinit var companyNumber: String

	private var personsActivityPlugin = TiActivityPlugin<PersonsPresenter, PersonsActivityView> {
		CompaniesHouseApplication.getInstance().applicationComponent.inject(this)
		personsPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {
		addPlugin(personsActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_persons)
		baseActivityPlugin.logScreenView(this.localClassName)

		ButterKnife.bind(this)
		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
			supportActionBar?.setTitle(R.string.persons_with_significant_control)
			toolbar?.setNavigationOnClickListener { onBackPressed() }
		}
		companyNumber = intent.getStringExtra("companyNumber")
		createPersonsRecyclerView()
	}

	private fun createPersonsRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		personsRecyclerView?.layoutManager = linearLayoutManager
		personsRecyclerView?.addItemDecoration(
				DividerItemDecoration(this))
	}

	override fun showProgress() {
		progressbar?.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progressbar?.visibility = View.GONE
	}


	override fun showPersons(persons: Persons) {
		if (personsRecyclerView?.adapter == null) {
			personsAdapter = PersonsAdapter(this@PersonsActivity, persons)
			personsRecyclerView?.adapter = personsAdapter
		} else {
			personsAdapter?.updateItems(persons)
		}
	}

	override fun showNoPersons() {
		lblNoPersons?.visibility = View.VISIBLE
		personsRecyclerView?.visibility = View.GONE
	}

	override fun personsItemClicked(v: View, position: Int, person: Person) {
		val gson = Gson()
		val jsonItem = gson.toJson(person, Person::class.java)
		val intent = Intent(this, PersonsDetailsActivity::class.java)
		intent.putExtra("personsItem", jsonItem)
		baseActivityPlugin.startActivityWithRightSlide(intent)
	}

	override fun showError() {
		Toast.makeText(this, R.string.could_not_retrieve_persons_info, Toast.LENGTH_LONG).show()
	}

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
