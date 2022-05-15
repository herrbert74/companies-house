package com.babestudios.companyinfouk.officers.ui.officers.list

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.databinding.RowOfficersBinding

class OfficersViewHolder(val rawBinding: ViewBinding) : RecyclerView.ViewHolder(rawBinding.root) {

	fun bind(officer: Officer) {
		val binding = rawBinding as RowOfficersBinding
		binding.lblOfficersName.text = officer.name
		binding.lblOfficersFromTo.text = officer.fromToString
		binding.lblOfficersRole.text = officer.officerRole
	}

}
