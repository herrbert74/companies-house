package com.babestudios.companyinfouk.officers.ui.officers.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import com.babestudios.companyinfouk.officers.databinding.RowOfficersBinding
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class OfficersAdapter(private var officersVisitables: List<AbstractOfficersVisitable>
					  , private val officersTypeFactory: OfficersTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<AbstractOfficersVisitable>>() {

	override fun getItemCount(): Int {
		return officersVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return officersVisitables[position].type(officersTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<AbstractOfficersVisitable>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<AbstractOfficersVisitable>> {
		return itemClickSubject
	}

	interface OfficersTypeFactory {
		fun type(officersItem: OfficerItem): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractOfficersVisitable> {
		val binding = RowOfficersBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false)
		val v = officersTypeFactory.holder(viewType, binding) as BaseViewHolder<AbstractOfficersVisitable>
		RxView.clicks(binding.root)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractOfficersVisitable>, position: Int) {
		holder.bind(officersVisitables[position])
	}

	fun updateItems(visitables: List<AbstractOfficersVisitable>) {
		officersVisitables = visitables
		notifyDataSetChanged()
	}
}
