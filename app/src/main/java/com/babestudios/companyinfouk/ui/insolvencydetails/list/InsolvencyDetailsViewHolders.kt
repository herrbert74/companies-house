package com.babestudios.companyinfouk.ui.insolvencydetails.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_insolvency_details_date.view.*
import kotlinx.android.synthetic.main.row_insolvency_details_practitioner.view.*
import kotlinx.android.synthetic.main.row_recent_searches_title.view.*

class InsolvencyDetailsTitleViewHolder(itemView: View) : BaseViewHolder<AbstractInsolvencyDetailsVisitable>(itemView) {
	override fun bind(visitable: AbstractInsolvencyDetailsVisitable) {
		val insolvencyDetailsTitleItem = (visitable as InsolvencyDetailsTitleVisitable).insolvencyDetailsTitleItem
		itemView.lblRecentSearchesTitle.text = insolvencyDetailsTitleItem.title
	}
}

class InsolvencyDetailsDateViewHolder(itemView: View) : BaseViewHolder<AbstractInsolvencyDetailsVisitable>(itemView) {
	override fun bind(visitable: AbstractInsolvencyDetailsVisitable) {
		val insolvencyDetailsDateItem = (visitable as InsolvencyDetailsDateVisitable).insolvencyDetailsDateItem
		itemView.lblInsolvencyDetailsDate.text = insolvencyDetailsDateItem.date
		itemView.lblInsolvencyDetailsType.text = insolvencyDetailsDateItem.type
	}
}

class InsolvencyDetailsPractitionerViewHolder(itemView: View) : BaseViewHolder<AbstractInsolvencyDetailsVisitable>(itemView) {
	override fun bind(visitable: AbstractInsolvencyDetailsVisitable) {
		val insolvencyDetailsPractitionerItem = (visitable as InsolvencyDetailsPractitionerVisitable).insolvencyDetailsPractitionerItem
		itemView.lblInsolvencyDetailsAppointedOn.text = insolvencyDetailsPractitionerItem.practitioner.appointedOn
		itemView.lblInsolvencyDetailsCeasedToActOn.text = insolvencyDetailsPractitionerItem.practitioner.ceasedToActOn
		itemView.lblInsolvencyDetailsPractitionerName.text = insolvencyDetailsPractitionerItem.practitioner.name
		itemView.lblInsolvencyDetailsPractitionerRole.text = insolvencyDetailsPractitionerItem.practitioner.role
		itemView.lblInsolvencyDetailsPractitionerAddressLine1.text = insolvencyDetailsPractitionerItem.practitioner.address?.addressLine1
		itemView.lblInsolvencyDetailsPractitionerLocality.text = insolvencyDetailsPractitionerItem.practitioner.address?.locality
		itemView.lblInsolvencyDetailsPractitionerPostalCode.text = insolvencyDetailsPractitionerItem.practitioner.address?.postalCode
		if (insolvencyDetailsPractitionerItem.practitioner.address?.region == null) {
			itemView.lblInsolvencyDetailsPractitionerRegion?.visibility = View.GONE
		} else {
			itemView.lblInsolvencyDetailsPractitionerRegion?.visibility = View.VISIBLE
			itemView.lblInsolvencyDetailsPractitionerRegion?.text = insolvencyDetailsPractitionerItem.practitioner.address?.region
		}
		if (insolvencyDetailsPractitionerItem.practitioner.address?.country == null) {
			itemView.lblInsolvencyDetailsPractitionerCountry?.visibility = View.GONE
		} else {
			itemView.lblInsolvencyDetailsPractitionerCountry?.visibility = View.VISIBLE
			itemView.lblInsolvencyDetailsPractitionerCountry?.text = insolvencyDetailsPractitionerItem.practitioner.address?.country
		}
	}
}