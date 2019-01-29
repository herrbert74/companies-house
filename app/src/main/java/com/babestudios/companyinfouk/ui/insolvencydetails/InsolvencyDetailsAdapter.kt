package com.babestudios.companyinfouk.ui.insolvencydetails

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.insolvency.Date
import com.babestudios.companyinfouk.data.model.insolvency.Practitioner

class InsolvencyDetailsAdapter(private val insolvencyDates: List<Date>, private val insolvencyPractitioners: List<Practitioner>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val itemLayoutView: View
		when (viewType) {
			TYPE_DATE -> {
				itemLayoutView = LayoutInflater.from(parent.context)
						.inflate(R.layout.row_insolvency_details_date, parent, false)

				return DatesViewHolder(itemLayoutView)
			}
			TYPE_PRACTITIONER -> {
				itemLayoutView = LayoutInflater.from(parent.context)
						.inflate(R.layout.row_insolvency_details_practitioner, parent, false)

				return PractitionersViewHolder(itemLayoutView)
			}
			TYPE_HEADER -> {
				itemLayoutView = LayoutInflater.from(parent.context)
						.inflate(R.layout.row_recent_searches_title, parent, false)

				return TitleViewHolder(itemLayoutView)
			}
			else -> throw RuntimeException("There is no type that matches the type $viewType + make sure your using types correctly")
		}
	}

	override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
		if (viewHolder is TitleViewHolder) {
			if (position == 0) {
				viewHolder.lblTitle?.text = CompaniesHouseApplication.context.getText(R.string.insolvency_dates)
			} else {
				viewHolder.lblTitle?.text = CompaniesHouseApplication.context.getText(R.string.insolvency_practitioners)
			}
		} else if (viewHolder is PractitionersViewHolder) {
			viewHolder.textViewAppointedOn?.text = insolvencyPractitioners[position - insolvencyDates.size - 2].appointedOn
			viewHolder.textViewCeasedToActOn?.text = insolvencyPractitioners[position - insolvencyDates.size - 2].ceasedToActOn
			viewHolder.textViewName?.text = insolvencyPractitioners[position - insolvencyDates.size - 2].name
			viewHolder.textViewRole?.text = insolvencyPractitioners[position - insolvencyDates.size - 2].role
			viewHolder.textViewAddressLine1?.text = insolvencyPractitioners[position - insolvencyDates.size - 2].address?.addressLine1
			viewHolder.textViewLocality?.text = insolvencyPractitioners[position - insolvencyDates.size - 2].address?.locality
			viewHolder.textViewPostalCode?.text = insolvencyPractitioners[position - insolvencyDates.size - 2].address?.postalCode
			if (insolvencyPractitioners[position - insolvencyDates.size - 2].address?.region == null) {
				viewHolder.textViewRegion?.visibility = View.GONE
			} else {
				viewHolder.textViewRegion?.visibility = View.VISIBLE
				viewHolder.textViewRegion?.text = insolvencyPractitioners[position - insolvencyDates.size - 2].address?.region
			}
			if (insolvencyPractitioners[position - insolvencyDates.size - 2].address?.country == null) {
				viewHolder.textViewCountry?.visibility = View.GONE
			} else {
				viewHolder.textViewCountry?.visibility = View.VISIBLE
				viewHolder.textViewCountry?.text = insolvencyPractitioners[position - insolvencyDates.size - 2].address?.country
			}
		} else if (viewHolder is DatesViewHolder) {
			viewHolder.textViewDate?.text = insolvencyDates[position - 1].date
			viewHolder.textViewType?.text = insolvencyDates[position - 1].type
		}

	}

	override fun getItemViewType(position: Int): Int {
		return when {
			isPositionHeader(position) -> TYPE_HEADER
			position < insolvencyDates.size + 1 -> TYPE_DATE
			else -> TYPE_PRACTITIONER
		}
	}

	private fun isPositionHeader(position: Int): Boolean {
		return position == 0 || position == insolvencyDates.size + 1
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return insolvencyDates.size + insolvencyPractitioners.size + 2
	}

	inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		@JvmField
		@BindView(R.id.lblTitle)
		var lblTitle: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
		}
	}

	inner class DatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		@JvmField
		@BindView(R.id.textViewDate)
		var textViewDate: TextView? = null
		@JvmField
		@BindView(R.id.textViewType)
		var textViewType: TextView? = null


		init {
			ButterKnife.bind(this, itemView)
		}
	}

	inner class PractitionersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		@JvmField
		@BindView(R.id.textViewAppointedOn)
		var textViewAppointedOn: TextView? = null
		@JvmField
		@BindView(R.id.textViewCeasedToActOn)
		var textViewCeasedToActOn: TextView? = null
		@JvmField
		@BindView(R.id.textViewName)
		var textViewName: TextView? = null
		@JvmField
		@BindView(R.id.textViewRole)
		var textViewRole: TextView? = null
		@JvmField
		@BindView(R.id.textViewAddressLine1)
		var textViewAddressLine1: TextView? = null
		@JvmField
		@BindView(R.id.textViewLocality)
		var textViewLocality: TextView? = null
		@JvmField
		@BindView(R.id.textViewPostalCode)
		var textViewPostalCode: TextView? = null
		@JvmField
		@BindView(R.id.textViewRegion)
		var textViewRegion: TextView? = null
		@JvmField
		@BindView(R.id.textViewCountry)
		var textViewCountry: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
		}
	}

	companion object {

		private const val TYPE_HEADER = 0
		private const val TYPE_DATE = 1
		private const val TYPE_PRACTITIONER = 2
	}
}
