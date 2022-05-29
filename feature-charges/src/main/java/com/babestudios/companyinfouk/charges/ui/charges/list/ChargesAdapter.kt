package com.babestudios.companyinfouk.charges.ui.charges.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.companyinfouk.charges.databinding.RowChargesBinding
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class ChargesAdapter(
	private var chargesItems: List<ChargesItem>,
	private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<ChargesViewHolder>() {

	private val itemClicksChannel: Channel<ChargesItem> = Channel(Channel.UNLIMITED)
	val itemClicks: Flow<ChargesItem> = itemClicksChannel.consumeAsFlow()

	override fun getItemCount(): Int {
		return chargesItems.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargesViewHolder {
		val binding = RowChargesBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)
		return ChargesViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ChargesViewHolder, position: Int) {
		holder.bind(chargesItems[position])
		holder.rawBinding.root.clicks().onEach {
			itemClicksChannel.trySend(chargesItems[position])
		}.launchIn(lifecycleScope)
	}

	fun updateItems(chargesItems: List<ChargesItem>) {
		this.chargesItems = chargesItems
		notifyDataSetChanged()
	}
}
