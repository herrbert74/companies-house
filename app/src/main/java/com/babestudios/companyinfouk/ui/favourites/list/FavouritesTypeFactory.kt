package com.babestudios.companyinfouk.ui.favourites.list

import android.view.View
import com.babestudios.companyinfouk.R
import com.babestudios.base.mvp.list.BaseViewHolder

class FavouritesTypeFactory : FavouritesAdapter.FavouritesTypeFactory {
	override fun type(favouritesItem: FavouritesItem): Int = R.layout.row_favourites

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_favourites -> FavouritesViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}