package com.babestudios.companyinfouk.companies.ui.favourites.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.companies.databinding.RowFavouritesBinding
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class FavouritesAdapter(private var favouritesVisitables: List<FavouritesVisitableBase>
						, private val favouritesTypeFactory: FavouritesTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<FavouritesVisitableBase>>() {

	override fun getItemCount(): Int {
		return favouritesVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return favouritesVisitables[position].type(favouritesTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<FavouritesVisitableBase>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<FavouritesVisitableBase>> {
		return itemClickSubject
	}

	private val cancelClickSubject = PublishSubject.create<BaseViewHolder<FavouritesVisitableBase>>()

	fun getCancelClickedObservable(): Observable<BaseViewHolder<FavouritesVisitableBase>> {
		return cancelClickSubject
	}

	interface FavouritesTypeFactory {
		fun type(favouritesListItem: FavouritesListItem): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<FavouritesVisitableBase>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<FavouritesVisitableBase> {
		val binding = RowFavouritesBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false)
		val v = favouritesTypeFactory.holder(viewType, binding)
		RxView.clicks(v.itemView)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		RxView.clicks(binding.btnFavouritesUndo)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(cancelClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<FavouritesVisitableBase>, position: Int) {
		holder.bind(favouritesVisitables[position])
	}

	fun updateItems(visitables: List<FavouritesVisitableBase>) {
		favouritesVisitables = visitables
		notifyDataSetChanged()
	}
}
