package com.babestudios.companyinfouk.ui.officers.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_officers.view.*

class OfficersViewHolder(itemView: View) : BaseViewHolder<AbstractOfficersVisitable>(itemView) {
	override fun bind(visitable: AbstractOfficersVisitable) {
		val officers = (visitable as OfficersVisitable).officersItem
		itemView.lblName?.text = officers.name
		itemView.lblAppointedOn?.text = officers.appointedOn
		itemView.lblRole?.text = officers.officerRole
		itemView.lblResignedOn?.text = officers.resignedOn
	}
}