package com.babestudios.companyinfouk.ui.insolvency.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_insolvency.view.*

class InsolvencyViewHolder(itemView: View) : BaseViewHolder<AbstractInsolvencyVisitable>(itemView) {
	override fun bind(visitable: AbstractInsolvencyVisitable) {
		val insolvencyCase = (visitable as InsolvencyVisitable).insolvencyCase
		itemView.lblDate.text = insolvencyCase.dates[0].date
		itemView.lblNumber?.text = insolvencyCase.number
		itemView.lblType?.text = insolvencyCase.type
		if (insolvencyCase.practitioners.isNotEmpty()) {
			itemView.lblPractitioner?.text = insolvencyCase.practitioners[0].name
		}
	}
}