package com.babestudios.companyinfouk.companies.ui.favourites.list

import androidx.recyclerview.widget.DiffUtil

class FavouritesDiffCallback(
	private val oldList: List<FavouritesItem>,
	private val newList: List<FavouritesItem>
) : DiffUtil.Callback() {

	override fun getOldListSize(): Int = oldList.size

	override fun getNewListSize(): Int = newList.size

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition].searchHistoryItem.companyNumber ==
			newList[newItemPosition].searchHistoryItem.companyNumber
	}

	override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
		return oldList[oldPosition].isPendingRemoval == newList[newPosition].isPendingRemoval &&
			oldList[oldPosition].searchHistoryItem == newList[newPosition].searchHistoryItem
	}

}
