package com.babestudios.companyinfouk.officers.ui.officers.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.officers.databinding.RowOfficersBinding

class OfficersViewHolder(_binding: ViewBinding) : BaseViewHolder<AbstractOfficersVisitable>(_binding) {
	override fun bind(visitable: AbstractOfficersVisitable) {
		val binding = _binding as RowOfficersBinding
		val officers = (visitable as OfficersVisitable).officerItem
		binding.lblOfficersName.text = officers.name
		binding.lblOfficersAppointedOn.text = officers.appointedOn
		binding.lblOfficersRole.text = officers.officerRole
		binding.lblOfficersResignedOn.text = officers.resignedOn
	}
}
