package com.babestudios.companyinfouk.persons.ui.persons.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.persons.Person
import com.babestudios.companyinfouk.persons.R

class PersonsTypeFactory : PersonsAdapter.PersonsTypeFactory {
	override fun type(persons: Person): Int = R.layout.row_persons

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_persons -> PersonsViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}