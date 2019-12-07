package com.babestudios.companyinfouk.companies.ui.favourites.list

import android.view.View
import com.babestudios.companyinfouk.companies.R
import com.babestudios.base.mvp.list.BaseViewHolder

class FavouritesTypeFactory : FavouritesAdapter.FavouritesTypeFactory {
	override fun type(favouritesListItem: FavouritesListItem): Int = R.layout.row_favourites

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_favourites -> FavouritesViewHolder(view)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
