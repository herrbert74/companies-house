package com.babestudios.companyinfouk.ui.chargesdetails

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.charges.ChargesItem

import butterknife.BindView
import butterknife.ButterKnife

internal class ChargesDetailsAdapter(var context: Context, private val chargesItem: ChargesItem) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val itemLayoutView: View
		if (viewType == TYPE_TRANSACTION) {
			itemLayoutView = LayoutInflater.from(parent.context)
					.inflate(R.layout.charges_details_transactions_list_item, parent, false)

			return TransactionsViewHolder(itemLayoutView)
		} else if (viewType == TYPE_HEADER) {
			itemLayoutView = LayoutInflater.from(parent.context)
					.inflate(R.layout.charges_details_header_item, parent, false)

			return HeaderViewHolder(itemLayoutView)
		}
		throw RuntimeException("There is no type that matches the type $viewType + make sure your using types correctly")
	}

	override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
		if (viewHolder is HeaderViewHolder) {
			viewHolder.textViewDeliveredOn!!.text = chargesItem.deliveredOn
			viewHolder.textViewStatus!!.text = chargesItem.status
			viewHolder.textViewContainsFloatingCharge!!.text = if (chargesItem.particulars.containsFloatingCharge) "YES" else "NO"
			viewHolder.textViewFloatingChargeCoversAll!!.text = if (chargesItem.particulars.floatingChargeCoversAll) "YES" else "NO"
			viewHolder.textViewContainsNegativePledge!!.text = if (chargesItem.particulars.containsNegativePledge) "YES" else "NO"
			viewHolder.textViewContainsFixedCharge!!.text = if (chargesItem.particulars.containsFixedCharge) "YES" else "NO"
			if (chargesItem.satisfiedOn == null) {
				viewHolder.textViewLabelSatisfiedOn!!.visibility = View.GONE
				viewHolder.textViewSatisfiedOn!!.visibility = View.GONE
			} else {
				viewHolder.textViewSatisfiedOn!!.visibility = View.VISIBLE
				viewHolder.textViewLabelSatisfiedOn!!.visibility = View.VISIBLE
				viewHolder.textViewSatisfiedOn!!.text = chargesItem.satisfiedOn
			}
			viewHolder.textViewPersonsEntitled!!.text = chargesItem.personsEntitled[0].name

		} else if (viewHolder is TransactionsViewHolder) {
			viewHolder.textViewFilingType!!.text = chargesItem.transactions[position - 1].filingType
			viewHolder.textViewDeliveredOn!!.text = chargesItem.transactions[position - 1].deliveredOn
		}

	}

	override fun getItemViewType(position: Int): Int {
		return if (isPositionHeader(position)) {
			TYPE_HEADER
		} else {
			TYPE_TRANSACTION
		}
	}

	private fun isPositionHeader(position: Int): Boolean {
		return position == 0
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return chargesItem.transactions.size + 1
	}

	inner class HeaderViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
		@JvmField
		@BindView(R.id.textViewDeliveredOn)
		internal var textViewDeliveredOn: TextView? = null
		@JvmField
		@BindView(R.id.textViewStatus)
		internal var textViewStatus: TextView? = null
		@JvmField
		@BindView(R.id.textViewContainsFloatingCharge)
		internal var textViewContainsFloatingCharge: TextView? = null
		@JvmField
		@BindView(R.id.textViewFloatingChargeCoversAll)
		internal var textViewFloatingChargeCoversAll: TextView? = null
		@JvmField
		@BindView(R.id.textViewContainsNegativePledge)
		internal var textViewContainsNegativePledge: TextView? = null
		@JvmField
		@BindView(R.id.textViewContainsFixedCharge)
		internal var textViewContainsFixedCharge: TextView? = null
		@JvmField
		@BindView(R.id.textViewLabelSatisfiedOn)
		internal var textViewLabelSatisfiedOn: TextView? = null
		@JvmField
		@BindView(R.id.textViewSatisfiedOn)
		internal var textViewSatisfiedOn: TextView? = null
		@JvmField
		@BindView(R.id.textViewPersonsEntitled)
		internal var textViewPersonsEntitled: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
		}
	}

	inner class TransactionsViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

		@JvmField
		@BindView(R.id.textViewFilingType)
		internal var textViewFilingType: TextView? = null
		@JvmField
		@BindView(R.id.textViewDeliveredOn)
		internal var textViewDeliveredOn: TextView? = null


		init {
			ButterKnife.bind(this, itemView)
		}
	}

	companion object {

		private val TYPE_HEADER = 0
		private val TYPE_TRANSACTION = 1
	}
}
