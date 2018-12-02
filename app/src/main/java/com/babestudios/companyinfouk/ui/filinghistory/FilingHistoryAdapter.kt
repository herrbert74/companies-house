package com.babestudios.companyinfouk.ui.filinghistory

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.DataManager
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FilingHistoryAdapter internal constructor(c: Context, filingHistoryList: FilingHistoryList, categoryFilter: FilingHistoryPresenter.CategoryFilter) : RecyclerView.Adapter<FilingHistoryAdapter.FilingHistoryViewHolder>() {

	private val mItemListener: FilingHistoryRecyclerViewClickListener

	private var filingHistoryList = FilingHistoryList()
	private var filteredFilingHistoryItems: List<FilingHistoryItem> = ArrayList()

	@Inject
	lateinit var dataManager: DataManager

	init {
		CompaniesHouseApplication.getInstance().applicationComponent.inject(this)
		mItemListener = c as FilingHistoryRecyclerViewClickListener
		this.filingHistoryList = filingHistoryList
		updateFilteredResults(filingHistoryList, categoryFilter)
	}

	@SuppressLint("CheckResult")
	private fun updateFilteredResults(filingHistoryList: FilingHistoryList, categoryFilter: FilingHistoryPresenter.CategoryFilter) {
		if (categoryFilter.ordinal > FilingHistoryPresenter.CategoryFilter.CATEGORY_SHOW_ALL.ordinal) {
			Observable.fromIterable(filingHistoryList.items).filter { filingHistoryItem -> filingHistoryItem.category.equals(categoryFilter.toString(), ignoreCase = true) }
					.observeOn(Schedulers.trampoline())
					.toList()
					.subscribe { result -> filteredFilingHistoryItems = result }
		} else {
			filteredFilingHistoryItems = filingHistoryList.items
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, i: Int): FilingHistoryViewHolder {
		val itemLayoutView = LayoutInflater.from(parent.context)
				.inflate(R.layout.filing_history_list_item, parent, false)

		return FilingHistoryViewHolder(itemLayoutView)
	}

	override fun onBindViewHolder(viewHolder: FilingHistoryViewHolder, position: Int) {
		val filingHistoryItem = filteredFilingHistoryItems[position]
		if (filingHistoryItem.description == "legacy" || filingHistoryItem.description == "miscellaneous") {
			viewHolder.lblDescription?.text = filingHistoryItem.descriptionValues?.description
		} else {
			filingHistoryItem.description?.let {
				val spannableDescription = FilingHistoryPresenter.createSpannableDescription(dataManager.filingHistoryLookup(it), filingHistoryItem)
				viewHolder.lblDescription?.text = spannableDescription
			}
		}
		viewHolder.lblDate?.text = filingHistoryItem.date
		viewHolder.lblCategory?.text = filingHistoryItem.category
		viewHolder.lblType?.text = filingHistoryItem.type

	}


	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return filteredFilingHistoryItems.size
	}

	fun setFilterOnAdapter(categoryFilter: FilingHistoryPresenter.CategoryFilter) {
		updateFilteredResults(filingHistoryList, categoryFilter)
		notifyDataSetChanged()
	}

	inner class FilingHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		@JvmField
		@BindView(R.id.lblDate)
		var lblDate: TextView? = null
		@JvmField
		@BindView(R.id.lblDescription)
		var lblDescription: TextView? = null
		@JvmField
		@BindView(R.id.lblCategory)
		var lblCategory: TextView? = null
		@JvmField
		@BindView(R.id.lblType)
		var lblType: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			mItemListener.filingItemClicked(v, this.layoutPosition, filteredFilingHistoryItems[layoutPosition])
		}
	}

	internal interface FilingHistoryRecyclerViewClickListener {
		fun filingItemClicked(v: View, position: Int, item: FilingHistoryItem)
	}

	fun updateItems(filingHistoryList: FilingHistoryList) {
		this.filingHistoryList = filingHistoryList
		notifyDataSetChanged()
	}
}
