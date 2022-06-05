package com.babestudios.companyinfouk.filings.ui.filinghistory.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.databinding.RowFilingHistoryBinding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class FilingHistoryAdapter internal constructor(
	private var filingHistoryItems: List<FilingHistoryItem>,
	private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<FilingHistoryViewHolder>() {

	private val itemClicksChannel: Channel<FilingHistoryItem> = Channel(Channel.UNLIMITED)
	val itemClicks: Flow<FilingHistoryItem> = itemClicksChannel.consumeAsFlow()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilingHistoryViewHolder {
		val binding = RowFilingHistoryBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false)
		return FilingHistoryViewHolder(binding)
	}

	override fun onBindViewHolder(viewHolder: FilingHistoryViewHolder, position: Int) {
		viewHolder.bind(filingHistoryItems[position])
		viewHolder.rawBinding.root.clicks().onEach {
			itemClicksChannel.trySend(filingHistoryItems[position])
		}.launchIn(lifecycleScope)
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return filingHistoryItems.size
	}

	fun updateItems(visitables: List<FilingHistoryItem>) {
		this.filingHistoryItems = visitables
		notifyDataSetChanged()
	}
}
