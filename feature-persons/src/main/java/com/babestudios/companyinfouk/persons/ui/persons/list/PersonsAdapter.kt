package com.babestudios.companyinfouk.persons.ui.persons.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.persons.Person
import com.babestudios.companyinfouk.persons.databinding.RowPersonsBinding
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class PersonsAdapter(
		private var personsVisitables: List<AbstractPersonsVisitable>,
		private val personsTypeFactory: PersonsTypeFactory
)
	: RecyclerView.Adapter<BaseViewHolder<AbstractPersonsVisitable>>() {

	override fun getItemCount(): Int {
		return personsVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return personsVisitables[position].type(personsTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<AbstractPersonsVisitable>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<AbstractPersonsVisitable>> {
		return itemClickSubject
	}

	interface PersonsTypeFactory {
		fun type(persons: Person): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractPersonsVisitable> {
		val binding = RowPersonsBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false)
		val v = personsTypeFactory.holder(viewType, binding) as BaseViewHolder<AbstractPersonsVisitable>
		RxView.clicks(binding.root)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractPersonsVisitable>, position: Int) {
		holder.bind(personsVisitables[position])
	}

	fun updateItems(visitables: List<AbstractPersonsVisitable>) {
		personsVisitables = visitables
		notifyDataSetChanged()
	}
}
