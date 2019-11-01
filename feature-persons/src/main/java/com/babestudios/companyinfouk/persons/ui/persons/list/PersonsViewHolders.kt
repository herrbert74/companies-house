package com.babestudios.companyinfouk.persons.ui.persons.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_persons.view.*

class PersonsViewHolder(itemView: View) : BaseViewHolder<AbstractPersonsVisitable>(itemView) {
	override fun bind(visitable: AbstractPersonsVisitable) {
		val person = (visitable as PersonsVisitable).person
		itemView.lblPersonsName?.text = person.name
		itemView.lblPersonsNotifiedOn?.text = person.notifiedOn
		itemView.lblPersonsNatureOfControl?.text = person.naturesOfControl[0]
		itemView.lblPersonsLocality?.text = person.address?.locality
	}
}