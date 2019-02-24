package com.babestudios.companyinfouk.ui.insolvencydetails.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.babestudios.base.mvp.list.BaseViewHolder

class InsolvencyDetailsAdapter(private var insolvencyDetailsVisitables: List<AbstractInsolvencyDetailsVisitable>
							   , private val insolvencyDetailsTypeFactory: InsolvencyDetailsTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<AbstractInsolvencyDetailsVisitable>>() {

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
		fun holder(type: Int, view: View): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractInsolvencyDetailsVisitable> {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		return insolvencyDetailsTypeFactory.holder(viewType, view) as BaseViewHolder<AbstractInsolvencyDetailsVisitable>
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractInsolvencyDetailsVisitable>, position: Int) {
		holder.bind(insolvencyDetailsVisitables[position])
	}

	fun updateItems(visitables: List<AbstractInsolvencyDetailsVisitable>) {
		insolvencyDetailsVisitables = visitables
		notifyDataSetChanged()
	}
}