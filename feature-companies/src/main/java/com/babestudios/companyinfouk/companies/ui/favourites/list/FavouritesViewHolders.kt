package com.babestudios.companyinfouk.companies.ui.favourites.list

import android.graphics.Color
import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_favourites.view.*

class FavouritesViewHolder(itemView: View) : BaseViewHolder<AbstractFavouritesVisitable>(itemView) {
	var favouritesVisitable: FavouritesVisitable? = null
	override fun bind(visitable: AbstractFavouritesVisitable) {
		favouritesVisitable = (visitable as FavouritesVisitable)
		val favouritesItem = favouritesVisitable!!.favouritesItem
		if (favouritesItem.isPendingRemoval) {
			itemView.setBackgroundColor(Color.RED)
			itemView.llFavourites?.visibility = View.INVISIBLE
			itemView.btnFavouritesUndo?.visibility = View.VISIBLE
		} else {
			itemView.setBackgroundColor(Color.WHITE)
			itemView.llFavourites?.visibility = View.VISIBLE
			itemView.btnFavouritesUndo?.visibility = View.GONE
			itemView.lblFavouritesCompanyName?.text = favouritesItem.searchHistoryItem.companyName
			itemView.lblFavouritesCompanyNumber?.text = favouritesItem.searchHistoryItem.companyNumber
			itemView.btnFavouritesUndo?.visibility = View.GONE
			itemView.btnFavouritesUndo?.setOnClickListener(null)
		}
	}
}