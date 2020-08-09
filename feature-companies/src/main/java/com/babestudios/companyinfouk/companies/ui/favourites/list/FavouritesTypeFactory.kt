package com.babestudios.companyinfouk.companies.ui.favourites.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.companies.R

class FavouritesTypeFactory : FavouritesAdapter.FavouritesTypeFactory {
	override fun type(favouritesListItem: FavouritesListItem): Int = R.layout.row_favourites

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<FavouritesVisitableBase> {
		return when (type) {
			R.layout.row_favourites -> FavouritesViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
