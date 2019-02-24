package com.babestudios.companyinfouk.ui.insolvencydetails.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_insolvency_details_date.view.*
import kotlinx.android.synthetic.main.row_insolvency_details_practitioner.view.*
import kotlinx.android.synthetic.main.row_recent_searches_title.view.*

class InsolvencyDetailsTitleViewHolder(itemView: View) : BaseViewHolder<AbstractInsolvencyDetailsVisitable>(itemView) {
	override fun bind(visitable: AbstractInsolvencyDetailsVisitable) {
		val insolvencyDetailsTitleItem = (visitable as InsolvencyDetailsTitleVisitable).insolvencyDetailsTitleItem
		itemView.lblTitle.text = insolvencyDetailsTitleItem.title
	}
}

class InsolvencyDetailsDateViewHolder(itemView: View) : BaseViewHolder<AbstractInsolvencyDetailsVisitable>(itemView) {
	override fun bind(visitable: AbstractInsolvencyDetailsVisitable) {
		val insolvencyDetailsDateItem = (visitable as InsolvencyDetailsDateVisitable).insolvencyDetailsDateItem
		itemView.textViewDate.text = insolvencyDetailsDateItem.date
		itemView.textViewType.text = insolvencyDetailsDateItem.type
	}
}

class InsolvencyDetailsPractitionerViewHolder(itemView: View) : BaseViewHolder<AbstractInsolvencyDetailsVisitable>(itemView) {
	override fun bind(visitable: AbstractInsolvencyDetailsVisitable) {
		val insolvencyDetailsPractitionerItem = (visitable as InsolvencyDetailsPractitionerVisitable).insolvencyDetailsPractitionerItem
		itemView.textViewAppointedOn.text = insolvencyDetailsPractitionerItem.practitioner.appointedOn
		itemView.textViewCeasedToActOn.text = insolvencyDetailsPractitionerItem.practitioner.ceasedToActOn
		itemView.textViewName.text = insolvencyDetailsPractitionerItem.practitioner.name
		itemView.textViewRole.text = insolvencyDetailsPractitionerItem.practitioner.role
		itemView.textViewAddressLine1.text = insolvencyDetailsPractitionerItem.practitioner.address?.addressLine1
		itemView.textViewLocality.text = insolvencyDetailsPractitionerItem.practitioner.address?.locality
		itemView.textViewPostalCode.text = insolvencyDetailsPractitionerItem.practitioner.address?.postalCode
		if (insolvencyDetailsPractitionerItem.practitioner.address?.region == null) {
			itemView.textViewRegion?.visibility = View.GONE
		} else {
			itemView.textViewRegion?.visibility = View.VISIBLE
			itemView.textViewRegion?.text = insolvencyDetailsPractitionerItem.practitioner.address?.region
		}
		if (insolvencyDetailsPractitionerItem.practitioner.address?.country == null) {
			itemView.textViewCountry?.visibility = View.GONE
		} else {
			itemView.textViewCountry?.visibility = View.VISIBLE
			itemView.textViewCountry?.text = insolvencyDetailsPractitionerItem.practitioner.address?.country
		}
	}
}