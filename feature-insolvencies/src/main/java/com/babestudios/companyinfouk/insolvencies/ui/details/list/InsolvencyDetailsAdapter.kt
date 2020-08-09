package com.babestudios.companyinfouk.insolvencies.ui.details.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.databinding.RowSubtitleBinding
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyDetailsDateBinding
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyDetailsPractitionerBinding

class InsolvencyDetailsAdapter(private var insolvencyDetailsVisitables: List<InsolvencyDetailsVisitableBase>
							   , private val insolvencyDetailsTypeFactory: InsolvencyDetailsTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<InsolvencyDetailsVisitableBase>>() {

	override fun getItemCount(): Int {
		return insolvencyDetailsVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return insolvencyDetailsVisitables[position].type(insolvencyDetailsTypeFactory)
	}

	interface InsolvencyDetailsTypeFactory {
		fun type(insolvencyDetailsTitleItem: InsolvencyDetailsTitleItem): Int
		fun type(insolvencyDetailsDateItem: InsolvencyDetailsDateItem): Int
		fun type(insolvencyDetailsPractitionerItem: InsolvencyDetailsPractitionerItem): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<InsolvencyDetailsVisitableBase>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
			: BaseViewHolder<InsolvencyDetailsVisitableBase> {
		val binding = when (viewType) {
			R.layout.row_subtitle -> RowSubtitleBinding.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false)
			R.layout.row_insolvency_details_date -> RowInsolvencyDetailsDateBinding.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false)
			else -> RowInsolvencyDetailsPractitionerBinding.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false)
		}
		return insolvencyDetailsTypeFactory.holder(viewType, binding)
	}

	override fun onBindViewHolder(holder: BaseViewHolder<InsolvencyDetailsVisitableBase>, position: Int) {
		holder.bind(insolvencyDetailsVisitables[position])
	}

	fun updateItems(visitables: List<InsolvencyDetailsVisitableBase>) {
		insolvencyDetailsVisitables = visitables
		notifyDataSetChanged()
	}
}
