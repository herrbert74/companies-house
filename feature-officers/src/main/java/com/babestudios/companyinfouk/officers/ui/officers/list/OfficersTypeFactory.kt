package com.babestudios.companyinfouk.officers.ui.officers.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import com.babestudios.companyinfouk.officers.R

class OfficersTypeFactory : OfficersAdapter.OfficersTypeFactory {
	override fun type(officersItem: OfficerItem): Int = R.layout.row_officers

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<OfficersVisitableBase> {
		return when (type) {
			R.layout.row_officers -> OfficersViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
