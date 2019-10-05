package com.babestudios.companyinfouk.ui.chargedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View.GONE
import android.view.View.VISIBLE
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.charges.ChargesItem
import com.babestudios.companyinfouk.data.model.charges.Transaction
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.ui.chargedetails.list.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_charge_details.*

private const val CHARGES_ITEM = "com.babestudios.companyinfouk.ui.charges_item"

class ChargeDetailsActivity : AppCompatActivity() {

	private var chargeDetailsAdapter: ChargeDetailsAdapter? = null

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_charge_details)
		logScreenView(this.localClassName)
		setSupportActionBar(pabChargeDetails.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabChargeDetails.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.charge_details_title)
		val chargesItem: ChargesItem? = intent?.extras?.getParcelable(CHARGES_ITEM)
		chargesItem?.let {
			showChargesItem(it)
			createRecyclerView(it)
		}
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun showChargesItem(chargesItem: ChargesItem) {
		tvChargeDetailsDeliveredOn?.text = chargesItem.deliveredOn
		tvChargeDetailsStatus?.text = chargesItem.status
		tvChargeDetailsContainsFloatingCharge?.text = if (chargesItem.particulars?.containsFloatingCharge == true) "YES" else "NO"
		tvChargeDetailsFloatingChargeCoversAll?.text = if (chargesItem.particulars?.floatingChargeCoversAll== true) "YES" else "NO"
		tvChargeDetailsContainsNegativePledge?.text = if (chargesItem.particulars?.containsNegativePledge== true) "YES" else "NO"
		tvChargeDetailsContainsFixedCharge?.text = if (chargesItem.particulars?.containsFixedCharge== true) "YES" else "NO"
		if (chargesItem.satisfiedOn == null) {
			lblChargeDetailsSatisfiedOn?.visibility = GONE
			tvChargeDetailsSatisfiedOn?.visibility = GONE
		} else {
			tvChargeDetailsSatisfiedOn?.visibility = VISIBLE
			lblChargeDetailsSatisfiedOn?.visibility = VISIBLE
			tvChargeDetailsSatisfiedOn?.text = chargesItem.satisfiedOn
		}
		tvChargeDetailsPersonsEntitled?.text = chargesItem.personsEntitled[0].name
	}

	private fun createRecyclerView(chargesItem: ChargesItem) {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvChargeDetails.layoutManager = linearLayoutManager
		val titlePositions = java.util.ArrayList<Int>()
		titlePositions.add(0)
		rvChargeDetails.addItemDecoration(DividerItemDecorationWithSubHeading(this, titlePositions))
		chargeDetailsAdapter = ChargeDetailsAdapter(convertToVisitables(chargesItem.transactions), ChargeDetailsTypeFactory())
		rvChargeDetails.adapter = chargeDetailsAdapter
	}

	private fun convertToVisitables(transactions: List<Transaction>): List<AbstractChargeDetailsVisitable> {
		val visitables = ArrayList<AbstractChargeDetailsVisitable>()
		visitables.add(ChargeDetailsHeaderVisitable(ChargeDetailsHeaderItem(getString(R.string.transactions))))
		visitables.addAll(transactions.map { item -> ChargeDetailsVisitable(item) })
		return visitables
	}

	override fun onBackPressed() {
		super.finish()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
	}

	//endregion
}

fun Context.createChargeDetailsIntent(chargesItem: ChargesItem): Intent {
	return Intent(this, ChargeDetailsActivity::class.java)
			.putExtra(CHARGES_ITEM, chargesItem)
}
