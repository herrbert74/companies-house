package com.babestudios.companyinfouk.persons.ui.persons.list

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.persons.databinding.RowPersonBinding

class PersonsViewHolder(val rawBinding: ViewBinding) : RecyclerView.ViewHolder(rawBinding.root) {

	fun bind(person: Person) {
		val binding = rawBinding as RowPersonBinding
		binding.lblPersonRowName.text = person.name
		binding.lblPersonRowKind.text = person.kind
	}

}
