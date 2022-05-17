package com.babestudios.companyinfouk.persons.ui.persons.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.persons.databinding.RowPersonBinding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class PersonsAdapter(
	private var persons: List<Person>,
	private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<PersonsViewHolder>() {

	private val itemClicksChannel: Channel<Person> = Channel(Channel.UNLIMITED)
	val itemClicks: Flow<Person> = itemClicksChannel.consumeAsFlow()

	override fun getItemCount(): Int {
		return persons.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonsViewHolder {
		val binding = RowPersonBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)

		return PersonsViewHolder(binding)
	}

	override fun onBindViewHolder(holder: PersonsViewHolder, position: Int) {
		holder.bind(persons[position])
		holder.rawBinding.root.clicks().onEach {
			itemClicksChannel.trySend(persons[position])
		}.launchIn(lifecycleScope)
	}

	fun updateItems(persons: List<Person>) {
		this.persons = persons
		notifyDataSetChanged()
	}

}
