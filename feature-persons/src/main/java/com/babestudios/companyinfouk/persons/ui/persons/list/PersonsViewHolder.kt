package com.babestudios.companyinfouk.persons.ui.persons.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.persons.databinding.RowPersonsBinding

class PersonsViewHolder(_binding: ViewBinding) : BaseViewHolder<AbstractPersonsVisitable>(_binding) {
	override fun bind(visitable: AbstractPersonsVisitable) {
		val binding = _binding as RowPersonsBinding
		val person = (visitable as PersonsVisitable).person
		binding.lblPersonsName.text = person.name
		binding.lblPersonsNotifiedOn.text = person.notifiedOn
		binding.lblPersonsNatureOfControl.text = person.naturesOfControl[0]
		binding.lblPersonsLocality.text = person.address?.locality
	}
}
