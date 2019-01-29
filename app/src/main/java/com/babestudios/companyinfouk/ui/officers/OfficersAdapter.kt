package com.babestudios.companyinfouk.ui.officers

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import com.babestudios.companyinfouk.data.model.officers.Officers

import butterknife.BindView
import butterknife.ButterKnife

class OfficersAdapter internal constructor(c: Context, officers: Officers) : RecyclerView.Adapter<OfficersAdapter.OfficersViewHolder>() {

	private val mItemListener: OfficersRecyclerViewClickListener

	private var officers = Officers()

	init {
		mItemListener = c as OfficersRecyclerViewClickListener
		this.officers = officers
	}

	override fun onCreateViewHolder(parent: ViewGroup, i: Int): OfficersViewHolder {
		val itemLayoutView = LayoutInflater.from(parent.context)
				.inflate(R.layout.row_officers, parent, false)

		return OfficersViewHolder(itemLayoutView)
	}

	override fun onBindViewHolder(viewHolder: OfficersViewHolder, position: Int) {

		viewHolder.lblName?.text = officers.items[position].name
		viewHolder.lblAppointedOn?.text = officers.items[position].appointedOn
		viewHolder.lblRole?.text = officers.items[position].officerRole
		viewHolder.lblResignedOn?.text = officers.items[position].resignedOn

	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return officers.items.size
	}

	inner class OfficersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		@JvmField
		@BindView(R.id.lblName)
		var lblName: TextView? = null
		@JvmField
		@BindView(R.id.lblAppointedOn)
		var lblAppointedOn: TextView? = null
		@JvmField
		@BindView(R.id.lblRole)
		var lblRole: TextView? = null
		@JvmField
		@BindView(R.id.lblResignedOn)
		var lblResignedOn: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			mItemListener.officersItemClicked(v, this.layoutPosition, officers.items[layoutPosition])
		}
	}

	internal interface OfficersRecyclerViewClickListener {
		fun officersItemClicked(v: View, position: Int, officerItem: OfficerItem)
	}

	internal fun updateItems(officers: Officers) {
		this.officers = officers
		notifyDataSetChanged()
	}
}
