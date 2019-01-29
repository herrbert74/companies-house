package com.babestudios.companyinfouk.ui.insolvency

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase

import butterknife.BindView
import butterknife.ButterKnife

class InsolvencyAdapter internal constructor(c: Context, insolvency: Insolvency) : RecyclerView.Adapter<InsolvencyAdapter.InsolvencyViewHolder>() {

	private val mItemListener: InsolvencyRecyclerViewClickListener

	private var insolvency = Insolvency()

	init {
		mItemListener = c as InsolvencyRecyclerViewClickListener
		this.insolvency = insolvency
	}

	override fun onCreateViewHolder(parent: ViewGroup, i: Int): InsolvencyViewHolder {
		val itemLayoutView = LayoutInflater.from(parent.context)
				.inflate(R.layout.row_insolvency, parent, false)

		return InsolvencyViewHolder(itemLayoutView)
	}

	override fun onBindViewHolder(viewHolder: InsolvencyViewHolder, position: Int) {

		viewHolder.lblDate?.text = insolvency.cases[position].dates[0].date
		viewHolder.lblNumber?.text = insolvency.cases[position].number
		viewHolder.lblType?.text = insolvency.cases[position].type
		if (insolvency.cases[position].practitioners.size > 0) {
			viewHolder.lblPractitioner?.text = insolvency.cases[position].practitioners[0].name
		}

	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return insolvency.cases.size
	}

	inner class InsolvencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		@JvmField
		@BindView(R.id.lblDate)
		var lblDate: TextView? = null
		@JvmField
		@BindView(R.id.lblType)
		var lblType: TextView? = null
		@JvmField
		@BindView(R.id.lblNumber)
		var lblNumber: TextView? = null
		@JvmField
		@BindView(R.id.lblPractitioner)
		var lblPractitioner: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			mItemListener.insolvencyItemClicked(v, this.layoutPosition, insolvency.cases[layoutPosition])
		}
	}

	internal interface InsolvencyRecyclerViewClickListener {
		fun insolvencyItemClicked(v: View, position: Int, insolvencyCase: InsolvencyCase)
	}

	internal fun updateItems(insolvency: Insolvency) {
		this.insolvency = insolvency
		notifyDataSetChanged()
	}
}
