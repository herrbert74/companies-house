package com.babestudios.companyinfouk.ui.insolvency.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_insolvency.view.*

class InsolvencyViewHolder(itemView: View) : BaseViewHolder<AbstractInsolvencyVisitable>(itemView) {
	override fun bind(visitable: AbstractInsolvencyVisitable) {
		val insolvencyCase = (visitable as InsolvencyVisitable).insolvencyCase
		itemView.lblInsolvencyDate.text = insolvencyCase.dates[0].date
		itemView.lblInsolvencyNumber?.text = insolvencyCase.number
		itemView.lblInsolvencyType?.text = insolvencyCase.type
		if (insolvencyCase.practitioners.isNotEmpty()) {
			itemView.lblInsolvencyPractitioner?.text = insolvencyCase.practitioners[0].name
		}
	}
}