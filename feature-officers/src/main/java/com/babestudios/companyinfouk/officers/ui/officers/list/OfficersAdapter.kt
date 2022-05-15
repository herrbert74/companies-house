package com.babestudios.companyinfouk.officers.ui.officers.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.databinding.RowOfficersBinding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class OfficersAdapter(
	private var officers: List<Officer>,
	private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<OfficersViewHolder>() {

	private val itemClicksChannel: Channel<Officer> = Channel(Channel.UNLIMITED)
	val itemClicks: Flow<Officer> = itemClicksChannel.consumeAsFlow()

	override fun getItemCount(): Int {
		return officers.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfficersViewHolder {
		val binding = RowOfficersBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)

		return OfficersViewHolder(binding)
	}

	override fun onBindViewHolder(holder: OfficersViewHolder, position: Int) {
		holder.bind(officers[position])
		holder.rawBinding.root.clicks().onEach {
			itemClicksChannel.trySend(officers[position])
		}.launchIn(lifecycleScope)
	}

	fun updateItems(officers: List<Officer>) {
		this.officers = officers
		notifyDataSetChanged()
	}
}
