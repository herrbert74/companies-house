package com.babestudios.companyinfouk.companies.ui.main.recents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.databinding.RowSubtitleBinding
import com.babestudios.companyinfouk.common.databinding.RowTwoLinesBinding
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class SearchHistoryAdapter(
	private var searchHistoryVisitables: List<SearchHistoryVisitableBase>,
	private val searchHistoryTypeFactory: SearchHistoryTypeFactory,
	private val lifecycleScope: LifecycleCoroutineScope,
) : RecyclerView.Adapter<BaseViewHolder<SearchHistoryVisitableBase>>() {

	override fun getItemCount(): Int {
		return searchHistoryVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return searchHistoryVisitables[position].type(searchHistoryTypeFactory)
	}

	private val itemClicksChannel: Channel<SearchHistoryItem> = Channel(Channel.UNLIMITED)
	val itemClicks: Flow<SearchHistoryItem> = itemClicksChannel.consumeAsFlow()

	interface SearchHistoryTypeFactory {
		fun type(searchHistoryItem: SearchHistoryItem): Int
		fun type(searchHistoryHeaderItem: SearchHistoryHeaderItem): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<SearchHistoryVisitableBase>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SearchHistoryVisitableBase> {
		val binding = when (viewType) {
			R.layout.row_two_lines -> RowTwoLinesBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
			else -> RowSubtitleBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)

		}
		return searchHistoryTypeFactory.holder(viewType, binding)
	}

	override fun onBindViewHolder(holder: BaseViewHolder<SearchHistoryVisitableBase>, position: Int) {
		holder.bind(searchHistoryVisitables[position])
		holder._binding.root.clicks().onEach {
			if (searchHistoryVisitables[position] is SearchHistoryVisitable)
				itemClicksChannel.trySend(
					(searchHistoryVisitables[position] as SearchHistoryVisitable).searchHistoryItem
				)
		}.launchIn(lifecycleScope)
	}

	fun updateItems(visitables: List<SearchHistoryVisitableBase>) {
		searchHistoryVisitables = visitables
		notifyDataSetChanged()
	}
}
