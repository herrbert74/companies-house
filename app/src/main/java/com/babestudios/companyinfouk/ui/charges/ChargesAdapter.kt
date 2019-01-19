package com.babestudios.companyinfouk.ui.charges

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.data.model.charges.ChargesItem

import butterknife.BindView
import butterknife.ButterKnife

class ChargesAdapter internal constructor(c: Context, charges: Charges, internal var companiesRepository: CompaniesRepository) : RecyclerView.Adapter<ChargesAdapter.ChargesViewHolder>() {

	private val mItemListener: ChargesRecyclerViewClickListener

	private var charges = Charges()

	init {
		mItemListener = c as ChargesRecyclerViewClickListener
		this.charges = charges
	}

	override fun onCreateViewHolder(parent: ViewGroup, i: Int): ChargesViewHolder {
		val itemLayoutView = LayoutInflater.from(parent.context)
				.inflate(R.layout.charges_list_item, parent, false)

		return ChargesViewHolder(itemLayoutView)
	}

	override fun onBindViewHolder(viewHolder: ChargesViewHolder, position: Int) {

		viewHolder.lblCreatedOn!!.text = charges.items[position].createdOn
		viewHolder.lblChargeCode!!.text = charges.items[position].chargeCode
		viewHolder.lblStatus!!.text = charges.items[position].status
		viewHolder.lblPersonEntitled!!.text = charges.items[position].personsEntitled[0].name

	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return charges.items.size
	}

	inner class ChargesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		@JvmField
		@BindView(R.id.lblCreatedOn)
		var lblCreatedOn: TextView? = null
		@JvmField
		@BindView(R.id.lblStatus)
		var lblStatus: TextView? = null
		@JvmField
		@BindView(R.id.lblChargeCode)
		var lblChargeCode: TextView? = null
		@JvmField
		@BindView(R.id.lblPersonEntitled)
		var lblPersonEntitled: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			mItemListener.chargesItemClicked(v, this.layoutPosition, charges.items[layoutPosition])
		}
	}

	internal interface ChargesRecyclerViewClickListener {
		fun chargesItemClicked(v: View, position: Int, chargesItem: ChargesItem)
	}

	internal fun updateItems(charges: Charges) {
		this.charges = charges
		notifyDataSetChanged()
	}
}
