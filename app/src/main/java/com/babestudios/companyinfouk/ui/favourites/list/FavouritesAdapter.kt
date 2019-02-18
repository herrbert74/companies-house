package com.babestudios.companyinfouk.ui.favourites.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.babestudios.base.mvp.list.BaseViewHolder
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.row_favourites.view.*

class FavouritesAdapter(private var favouritesVisitables: List<AbstractFavouritesVisitable>
						, private val favouritesTypeFactory: FavouritesTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<AbstractFavouritesVisitable>>() {

	override fun getItemCount(): Int {
		return favouritesVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return favouritesVisitables[position].type(favouritesTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<AbstractFavouritesVisitable>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<AbstractFavouritesVisitable>> {
		return itemClickSubject
	}

	private val cancelClickSubject = PublishSubject.create<BaseViewHolder<AbstractFavouritesVisitable>>()

	fun getCancelClickedObservable(): Observable<BaseViewHolder<AbstractFavouritesVisitable>> {
		return cancelClickSubject
	}

	interface FavouritesTypeFactory {
		fun type(favouritesItem: FavouritesItem): Int
		fun holder(type: Int, view: View): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractFavouritesVisitable> {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		val v = favouritesTypeFactory.holder(viewType, view) as BaseViewHolder<AbstractFavouritesVisitable>
		RxView.clicks(view)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		RxView.clicks(v.itemView.btnFavouritesUndo)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(cancelClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractFavouritesVisitable>, position: Int) {
		holder.bind(favouritesVisitables[position])
	}

	fun updateItems(visitables: List<AbstractFavouritesVisitable>) {
		favouritesVisitables = visitables
		notifyDataSetChanged()
	}
}