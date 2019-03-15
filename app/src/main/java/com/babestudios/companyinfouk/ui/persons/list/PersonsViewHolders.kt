package com.babestudios.companyinfouk.ui.persons.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_persons.view.*

class Persons2ViewHolder(itemView: View) : BaseViewHolder<AbstractPersonsVisitable>(itemView) {
	override fun bind(visitable: AbstractPersonsVisitable) {
		val person = (visitable as PersonsVisitable).person
		itemView.lblName?.text = person.name
		itemView.lblNotifiedOn?.text = person.notifiedOn
		itemView.lblNatureOfControl?.text = person.naturesOfControl[0]
		itemView.lblLocality?.text = person.address?.locality
	}
}