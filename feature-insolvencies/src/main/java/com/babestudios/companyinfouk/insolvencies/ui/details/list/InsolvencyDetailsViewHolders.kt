package com.babestudios.companyinfouk.insolvencies.ui.details.list

import android.view.View
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.databinding.RowSubtitleBinding
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyDetailsDateBinding
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyDetailsPractitionerBinding

class InsolvencyDetailsTitleViewHolder(_binding: ViewBinding)
	: BaseViewHolder<InsolvencyDetailsVisitableBase>(_binding) {
	override fun bind(visitable: InsolvencyDetailsVisitableBase) {
		val binding = _binding as RowSubtitleBinding
		val insolvencyDetailsTitleItem = (visitable as InsolvencyDetailsTitleVisitable).insolvencyDetailsTitleItem
		binding.lblCommonSubtitle.text = insolvencyDetailsTitleItem.title
	}
}

class InsolvencyDetailsDateViewHolder(_binding: ViewBinding) 
	: BaseViewHolder<InsolvencyDetailsVisitableBase>(_binding) {
	override fun bind(visitable: InsolvencyDetailsVisitableBase) {
		val binding = _binding as RowInsolvencyDetailsDateBinding
		val insolvencyDetailsDateItem = 
				(visitable as InsolvencyDetailsDateVisitable).insolvencyDetailsDateItem
		binding.lblInsolvencyDetailsDate.text = insolvencyDetailsDateItem.date
		binding.lblInsolvencyDetailsType.text = insolvencyDetailsDateItem.type
	}
}

class InsolvencyDetailsPractitionerViewHolder(_binding: ViewBinding)
	: BaseViewHolder<InsolvencyDetailsVisitableBase>(_binding) {
	override fun bind(visitable: InsolvencyDetailsVisitableBase) {
		val binding = _binding as RowInsolvencyDetailsPractitionerBinding
		val insolvencyDetailsPractitionerItem =
				(visitable as InsolvencyDetailsPractitionerVisitable)
						.insolvencyDetailsPractitionerItem
		binding.lblInsolvencyDetailsAppointedOn.text =
				insolvencyDetailsPractitionerItem.practitioner.appointedOn
		binding.lblInsolvencyDetailsCeasedToActOn.text =
				insolvencyDetailsPractitionerItem.practitioner.ceasedToActOn
		binding.lblInsolvencyDetailsPractitionerName.text =
				insolvencyDetailsPractitionerItem.practitioner.name
		binding.lblInsolvencyDetailsPractitionerRole.text =
				insolvencyDetailsPractitionerItem.practitioner.role
		binding.lblInsolvencyDetailsPractitionerAddressLine1.text =
				insolvencyDetailsPractitionerItem.practitioner.address?.addressLine1
		binding.lblInsolvencyDetailsPractitionerLocality.text =
				insolvencyDetailsPractitionerItem.practitioner.address?.locality
		binding.lblInsolvencyDetailsPractitionerPostalCode.text =
				insolvencyDetailsPractitionerItem.practitioner.address?.postalCode
		if (insolvencyDetailsPractitionerItem.practitioner.address?.region == null) {
			binding.lblInsolvencyDetailsPractitionerRegion.visibility = View.GONE
		} else {
			binding.lblInsolvencyDetailsPractitionerRegion.visibility = View.VISIBLE
			binding.lblInsolvencyDetailsPractitionerRegion.text =
					insolvencyDetailsPractitionerItem.practitioner.address?.region
		}
		if (insolvencyDetailsPractitionerItem.practitioner.address?.country == null) {
			binding.lblInsolvencyDetailsPractitionerCountry.visibility = View.GONE
		} else {
			binding.lblInsolvencyDetailsPractitionerCountry.visibility = View.VISIBLE
			binding.lblInsolvencyDetailsPractitionerCountry.text =
					insolvencyDetailsPractitionerItem.practitioner.address?.country
		}
	}
}
