package com.babestudios.companyinfouk.officers.ui.officers.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_officers.view.*

class OfficersViewHolder(itemView: View) : BaseViewHolder<AbstractOfficersVisitable>(itemView) {
	override fun bind(visitable: AbstractOfficersVisitable) {
		val officers = (visitable as OfficersVisitable).officerItem
		itemView.lblOfficersName?.text = officers.name
		itemView.lblOfficersAppointedOn?.text = officers.appointedOn
		itemView.lblOfficersRole?.text = officers.officerRole
		itemView.lblOfficersResignedOn?.text = officers.resignedOn
	}
}