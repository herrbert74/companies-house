package com.babestudios.companyinfouk.companies.ui.favourites.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.companyinfouk.companies.databinding.RowFavouritesBinding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class FavouritesAdapter(
	private var favouritesItems: List<FavouritesItem>,
	private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<FavouritesViewHolder>() {

	override fun getItemCount(): Int {
		return favouritesItems.size
	}

	private val itemClicksChannel: Channel<FavouritesItem> = Channel(Channel.UNLIMITED)
	val itemClicks: Flow<FavouritesItem> = itemClicksChannel.consumeAsFlow()

	private val cancelClicksChannel: Channel<FavouritesItem> = Channel(Channel.UNLIMITED)
	val cancelClicks: Flow<FavouritesItem> = cancelClicksChannel.consumeAsFlow()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
		val binding = RowFavouritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return FavouritesViewHolder(binding)
	}

	override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
		holder.bind(favouritesItems[position])

		holder.rawBinding.root.clicks().onEach {
			itemClicksChannel.trySend(favouritesItems[position])
		}.launchIn(lifecycleScope)

		(holder.rawBinding as RowFavouritesBinding).btnFavouritesUndo.clicks().onEach {
			cancelClicksChannel.trySend(favouritesItems[position])
		}.launchIn(lifecycleScope)
	}

	fun updateItems(newFavouritesItems: List<FavouritesItem>) {
		val diffCallback = FavouritesDiffCallback(favouritesItems, newFavouritesItems)
		val diffResult = DiffUtil.calculateDiff(diffCallback)
		favouritesItems = newFavouritesItems
		diffResult.dispatchUpdatesTo(this)
	}
}
