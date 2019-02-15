package com.babestudios.companyinfouk.ui.favourites.list

import android.graphics.Color
import android.view.View
import kotlinx.android.synthetic.main.row_favourites.view.*
import com.babestudios.base.mvp.list.BaseViewHolder

class FavouritesViewHolder(itemView: View) : BaseViewHolder<AbstractFavouritesVisitable>(itemView) {
	override fun bind(visitable: AbstractFavouritesVisitable) {
		val favouritesItem = (visitable as FavouritesVisitable).favouritesItem
		if (favouritesItem.isPendingRemoval) {
			itemView.setBackgroundColor(Color.RED)
			itemView.llFavourites?.visibility = View.INVISIBLE
			itemView.btnFavouritesUndo?.visibility = View.VISIBLE
		} else {
			itemView.lblFavouritesCompanyName?.text = favouritesItem.searchHistoryItem.companyName
			itemView.lblFavouritesCompanyNumber?.text = favouritesItem.searchHistoryItem.companyNumber
			itemView.btnFavouritesUndo?.visibility = View.GONE
			itemView.btnFavouritesUndo?.setOnClickListener(null)
		}
	}
}