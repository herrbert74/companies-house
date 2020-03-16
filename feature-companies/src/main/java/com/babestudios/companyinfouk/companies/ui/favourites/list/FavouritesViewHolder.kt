package com.babestudios.companyinfouk.companies.ui.favourites.list

import android.graphics.Color
import android.view.View
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.companies.databinding.RowFavouritesBinding

class FavouritesViewHolder(_binding: ViewBinding) : BaseViewHolder<AbstractFavouritesVisitable>(_binding) {

	var favouritesVisitable: FavouritesVisitable? = null
	override fun bind(visitable: AbstractFavouritesVisitable) {
		val binding = _binding as RowFavouritesBinding
				favouritesVisitable = (visitable as FavouritesVisitable)
		val favouritesItem = favouritesVisitable!!.favouritesListItem
		if (favouritesItem.isPendingRemoval) {
			binding.root.setBackgroundColor(Color.RED)
			binding.llFavourites.visibility = View.INVISIBLE
			binding.btnFavouritesUndo.visibility = View.VISIBLE
		} else {
			binding.root.setBackgroundColor(Color.WHITE)
			binding.llFavourites.visibility = View.VISIBLE
			binding.btnFavouritesUndo.visibility = View.GONE
			binding.lblFavouritesCompanyName.text = favouritesItem.searchHistoryItem.companyName
			binding.lblFavouritesCompanyNumber.text = favouritesItem.searchHistoryItem.companyNumber
			binding.btnFavouritesUndo.visibility = View.GONE
			binding.btnFavouritesUndo.setOnClickListener(null)
		}
	}
}
