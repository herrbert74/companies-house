package com.babestudios.companyinfouk.ui.persons.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfo.data.model.persons.Person

class PersonsTypeFactory : PersonsAdapter.PersonsTypeFactory {
	override fun type(persons: Person): Int = R.layout.row_persons

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_persons -> Persons2ViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}