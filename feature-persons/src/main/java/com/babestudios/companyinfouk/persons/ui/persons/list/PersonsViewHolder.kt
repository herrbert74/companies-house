package com.babestudios.companyinfouk.persons.ui.persons.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.persons.databinding.RowPersonBinding

class PersonsViewHolder(_binding: ViewBinding) : BaseViewHolder<PersonsVisitableBase>(_binding) {
	override fun bind(visitable: PersonsVisitableBase) {
		val binding = _binding as RowPersonBinding
		val person = (visitable as PersonsVisitable).person
		binding.lblPersonRowName.text = person.name
		binding.lblPersonRowKind.text = person.kind
	}
}
