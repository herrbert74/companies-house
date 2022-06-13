package com.babestudios.companyinfouk.companies.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.companyinfouk.companies.databinding.RowSearchResultBinding
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class SearchAdapter(
	private var companySearchResultItems: List<CompanySearchResultItem>,
	private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<SearchViewHolder>() {

	override fun getItemCount(): Int {
		return companySearchResultItems.size
	}

	private val itemClicksChannel: Channel<CompanySearchResultItem> = Channel(Channel.UNLIMITED)
	val itemClicks: Flow<CompanySearchResultItem> = itemClicksChannel.consumeAsFlow()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
		val binding = RowSearchResultBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)
		return SearchViewHolder((binding))
	}

	override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
		holder.bind(companySearchResultItems[position])
		holder.rawBinding.root.clicks().onEach {
			itemClicksChannel.trySend(companySearchResultItems[position])
		}.launchIn(lifecycleScope)
	}

	fun updateItems(companySearchResultItems: List<CompanySearchResultItem>) {
		this.companySearchResultItems = companySearchResultItems
		notifyDataSetChanged()
	}
}
