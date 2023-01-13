package com.babestudios.companyinfouk.companies.ui.favourites.list

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.companyinfouk.companies.databinding.RowFavouritesBinding

class FavouritesViewHolder(val rawBinding: ViewBinding) : RecyclerView.ViewHolder(rawBinding.root) {

	lateinit var favouritesItem: FavouritesItem

	fun bind(favouritesItem: FavouritesItem) {
		this.favouritesItem = favouritesItem
		val binding = rawBinding as RowFavouritesBinding
		if (favouritesItem.isPendingRemoval) {
			binding.root.setBackgroundColor(Color.RED)
			binding.llFavourites.visibility = View.INVISIBLE
			binding.btnFavouritesUndo.visibility = View.VISIBLE
		} else {
			binding.root.setBackgroundColor(Color.WHITE)
			binding.llFavourites.visibility = View.VISIBLE
			binding.lblFavouritesCompanyName.text = favouritesItem.searchHistoryItem.companyName
			binding.lblFavouritesCompanyNumber.text = favouritesItem.searchHistoryItem.companyNumber
			binding.btnFavouritesUndo.visibility = View.GONE
			binding.btnFavouritesUndo.setOnClickListener(null)
		}
	}

}
