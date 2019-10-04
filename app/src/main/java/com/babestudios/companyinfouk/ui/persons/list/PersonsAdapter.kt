package com.babestudios.companyinfouk.ui.persons.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfo.data.model.persons.Person
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class PersonsAdapter(private var personsVisitables: List<AbstractPersonsVisitable>, private val personsTypeFactory: PersonsTypeFactory)
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
		fun holder(type: Int, view: View): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractPersonsVisitable> {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		val v = personsTypeFactory.holder(viewType, view) as BaseViewHolder<AbstractPersonsVisitable>
		RxView.clicks(view)
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