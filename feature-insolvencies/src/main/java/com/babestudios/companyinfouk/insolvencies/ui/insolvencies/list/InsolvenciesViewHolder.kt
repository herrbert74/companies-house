package com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyBinding

class InsolvenciesViewHolder(val rawBinding: ViewBinding) : RecyclerView.ViewHolder(rawBinding.root) {

	fun bind(insolvencyCase: InsolvencyCase) {
		val binding = rawBinding as RowInsolvencyBinding
		binding.lblInsolvencyDate.text = insolvencyCase.dates[0].date
		binding.lblInsolvencyType.text = insolvencyCase.type
	}

}
