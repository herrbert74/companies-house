package com.babestudios.companyinfouk.officers.ui.officers.list

import android.view.View
import com.babestudios.companyinfouk.officers.R
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.officers.OfficerItem

class OfficersTypeFactory : OfficersAdapter.OfficersTypeFactory {
	override fun type(officersItem: OfficerItem): Int = R.layout.row_officers

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_officers -> OfficersViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}