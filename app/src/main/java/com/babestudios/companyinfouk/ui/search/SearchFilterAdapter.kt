package com.babestudios.companyinfouk.ui.search


import android.content.Context
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.utils.ResourceHelper


class SearchFilterAdapter(context: Context, private val mTexts: Array<String>, private val isDarkTheme: Boolean) : ArrayAdapter<String>(context, R.layout.menu_spinner_item, mTexts) {
	private val verticalDropdownPadding: Int = context.resources.getDimensionPixelSize(R.dimen.view_margin_small)

	override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
		var convertViewCopy = convertView
		val holder: DropDownViewHolder
		if (convertViewCopy == null) {
			val inflater = LayoutInflater.from(parent.context)
			convertViewCopy = inflater.inflate(R.layout.menu_spinner_dropdown_item, parent, false)
			holder = DropDownViewHolder()
			holder.llDropdownItemRoot = convertViewCopy!!.findViewById(R.id.llDropdownItemRoot)
			holder.lblDropdownItem = convertViewCopy.findViewById<View>(R.id.lblDropdownItem) as TextView
			convertViewCopy.tag = holder
		} else {
			holder = convertViewCopy.tag as DropDownViewHolder
		}
		var bottomPadding = 0
		var topPadding = 0
		if (position == 0) {
			topPadding = verticalDropdownPadding
		}
		if (position == count - 1) {
			bottomPadding = verticalDropdownPadding
		}

		if (isDarkTheme) {
			holder.llDropdownItemRoot?.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
		}

		holder.llDropdownItemRoot?.setPadding(0, topPadding, 0, bottomPadding)
		holder.lblDropdownItem?.text = mTexts[position]

		return convertViewCopy
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var convertViewCopy = convertView
		val holder: SpinnerItemViewHolder
		if (convertViewCopy == null) {
			val inflater = LayoutInflater.from(parent.context)
			convertViewCopy = inflater.inflate(R.layout.menu_spinner_item, parent, false)
			holder = SpinnerItemViewHolder()
			holder.llSpinnerItemRoot = convertViewCopy!!.findViewById(R.id.llSpinnerItemRoot)
			holder.ivSpinnerItem = convertViewCopy.findViewById<View>(R.id.ivSpinnerItem) as ImageView
			holder.lblSpinnerItem = convertViewCopy.findViewById<View>(R.id.lblSpinnerItem) as TextView
			holder.lblSpinnerItem?.setTextColor(ContextCompat.getColor(context, android.R.color.white))
			val vector = VectorDrawableCompat.create(context.resources, R.drawable.ic_arrow_drop_down, context.theme)
			vector?.let {
				holder.ivSpinnerItem?.setImageDrawable(ResourceHelper.tintVectorDrawableCompat(context, it, android.R.color.white))
			}
			convertViewCopy.tag = holder
		} else {
			holder = convertViewCopy.tag as SpinnerItemViewHolder
		}


		holder.lblSpinnerItem?.text = mTexts[position]
		return convertViewCopy
	}

	private class DropDownViewHolder {
		internal var llDropdownItemRoot: View? = null
		internal var lblDropdownItem: TextView? = null
	}

	private class SpinnerItemViewHolder {
		internal var llSpinnerItemRoot: View? = null
		internal var ivSpinnerItem: ImageView? = null
		internal var lblSpinnerItem: TextView? = null
	}
}
