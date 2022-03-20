package com.babestudios.companyinfouk.persons.ui.persons.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.persons.R
import java.lang.IllegalStateException

class PersonsTypeFactory : PersonsAdapter.PersonsTypeFactory {
	override fun type(persons: Person): Int = R.layout.row_person

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<PersonsVisitableBase> {
		return when (type) {
			R.layout.row_person -> PersonsViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
