package com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyBinding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class InsolvenciesAdapter(
	private var insolvencyCases: List<InsolvencyCase>,
	private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<InsolvenciesViewHolder>() {

	override fun getItemCount(): Int {
		return insolvencyCases.size
	}

	private val itemClicksChannel: Channel<InsolvencyCase> = Channel(Channel.UNLIMITED)
	val itemClicks: Flow<InsolvencyCase> = itemClicksChannel.consumeAsFlow()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsolvenciesViewHolder {
		val binding = RowInsolvencyBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)
		return InsolvenciesViewHolder(binding)
	}

	override fun onBindViewHolder(holder: InsolvenciesViewHolder, position: Int) {
		holder.bind(insolvencyCases[position])
		holder.rawBinding.root.clicks().onEach {
			itemClicksChannel.trySend(insolvencyCases[position])
		}.launchIn(lifecycleScope)
	}

	fun updateItems(insolvencyCases: List<InsolvencyCase>) {
		this.insolvencyCases = insolvencyCases
		notifyDataSetChanged()
	}
}
