package com.babestudios.companyinfouk.ui.persons

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.persons.Person
import com.babestudios.companyinfouk.data.model.persons.Persons

import butterknife.BindView
import butterknife.ButterKnife

class PersonsAdapter internal constructor(c: Context, persons: Persons) : RecyclerView.Adapter<PersonsAdapter.PersonsViewHolder>() {

	private val mItemListener: PersonsRecyclerViewClickListener

	private var persons = Persons()

	init {
		mItemListener = c as PersonsRecyclerViewClickListener
		this.persons = persons
	}

	override fun onCreateViewHolder(parent: ViewGroup, i: Int): PersonsViewHolder {
		val itemLayoutView = LayoutInflater.from(parent.context)
				.inflate(R.layout.row_persons, parent, false)

		return PersonsViewHolder(itemLayoutView)
	}

	override fun onBindViewHolder(viewHolder: PersonsViewHolder, position: Int) {
		viewHolder.lblName?.text = persons.items[position].name
		viewHolder.lblNotifiedOn?.text = persons.items[position].notifiedOn
		viewHolder.lblNatureOfControl?.text = persons.items[position].naturesOfControl[0]
		viewHolder.lblLocality?.text = persons.items[position].address?.locality
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return persons.items.size
	}

	inner class PersonsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		@JvmField
		@BindView(R.id.lblName)
		var lblName: TextView? = null
		@JvmField
		@BindView(R.id.lblNotifiedOn)
		var lblNotifiedOn: TextView? = null
		@JvmField
		@BindView(R.id.lblNatureOfControl)
		var lblNatureOfControl: TextView? = null
		@JvmField
		@BindView(R.id.lblLocality)
		var lblLocality: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			mItemListener.personsItemClicked(v, this.layoutPosition, persons.items[layoutPosition])
		}
	}

	interface PersonsRecyclerViewClickListener {
		fun personsItemClicked(v: View, position: Int, person: Person)
	}

	internal fun updateItems(persons: Persons) {
		this.persons = persons
		notifyDataSetChanged()
	}
}
