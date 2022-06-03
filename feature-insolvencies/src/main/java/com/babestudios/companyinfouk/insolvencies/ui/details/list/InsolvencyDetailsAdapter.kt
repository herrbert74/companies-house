package com.babestudios.companyinfouk.insolvencies.ui.details.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.databinding.RowSubtitleBinding
import com.babestudios.companyinfouk.common.databinding.RowTwoLinesBinding
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyDetailsPractitionerBinding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class InsolvencyDetailsAdapter(
	private var insolvencyDetailsVisitables: List<InsolvencyDetailsVisitableBase>,
	private val insolvencyDetailsTypeFactory: InsolvencyDetailsTypeFactory,
	private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<BaseViewHolder<InsolvencyDetailsVisitableBase>>() {

	override fun getItemCount(): Int {
		return insolvencyDetailsVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return insolvencyDetailsVisitables[position].type(insolvencyDetailsTypeFactory)
	}

	private val itemClicksChannel: Channel<Practitioner> = Channel(Channel.UNLIMITED)
	val itemClicks: Flow<Practitioner> = itemClicksChannel.consumeAsFlow()

	interface InsolvencyDetailsTypeFactory {
		fun type(insolvencyDetailsTitleItem: InsolvencyDetailsTitleItem): Int
		fun type(insolvencyDetailsDateItem: InsolvencyDetailsDateItem): Int
		fun type(practitioner: Practitioner): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<InsolvencyDetailsVisitableBase>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
		: BaseViewHolder<InsolvencyDetailsVisitableBase> {
		val binding = when (viewType) {
			R.layout.row_subtitle -> RowSubtitleBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
			R.layout.row_two_lines -> RowTwoLinesBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
			else -> RowInsolvencyDetailsPractitionerBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		}
		return insolvencyDetailsTypeFactory.holder(viewType, binding)
	}

	override fun onBindViewHolder(holder: BaseViewHolder<InsolvencyDetailsVisitableBase>, position: Int) {
		holder.bind(insolvencyDetailsVisitables[position])
		holder._binding.root.clicks()
			.filter { insolvencyDetailsVisitables[position] is InsolvencyDetailsPractitionerVisitable }
			.onEach {
				itemClicksChannel.trySend(
					(insolvencyDetailsVisitables[position] as InsolvencyDetailsPractitionerVisitable).practitioner
				)
			}.launchIn(lifecycleScope)
	}

	fun updateItems(visitables: List<InsolvencyDetailsVisitableBase>) {
		insolvencyDetailsVisitables = visitables
		notifyDataSetChanged()
	}
}
