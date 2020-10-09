package com.babestudios.companyinfouk.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.babestudios.companyinfouk.common.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.view_address.view.*


@Suppress("unused")
class AddressView @JvmOverloads constructor(
		context: Context,
		attrs: AttributeSet? = null,
		defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

	init {
		View.inflate(context, R.layout.view_address, this)
		obtainStyleAttributes(context, attrs)
	}

	private fun obtainStyleAttributes(context: Context, attrs: AttributeSet?) {
		context.theme.obtainStyledAttributes(attrs, R.styleable.AddressView, 0, 0).apply {
			cpnAddressTitle.text = getString(R.styleable.AddressView_title)
			lblAddressAddressLine1.text = getString(R.styleable.AddressView_addressLine1)
			lblAddressAddressLine2.text = getString(R.styleable.AddressView_addressLine2)
			lblAddressLocality.text = getString(R.styleable.AddressView_locality)
			lblAddressPostalCode.text = getString(R.styleable.AddressView_postCode)
			lblAddressRegion.text = getString(R.styleable.AddressView_region)
			lblAddressCountry.text = getString(R.styleable.AddressView_country)
		}.recycle()
	}

	fun setTitle(text: String?) {
		cpnAddressTitle.text = text
	}

	fun setAddressLine1(text: String?) {
		lblAddressAddressLine1.text = text
	}

	fun setAddressLine2(text: String?) {
		if (text.isNullOrEmpty()) {
			lblAddressAddressLine2.visibility = GONE
		} else {
			lblAddressAddressLine2.visibility = VISIBLE
			lblAddressAddressLine2.text = text
		}
	}

	fun setLocality(text: String?) {
		if (text.isNullOrEmpty()) {
			lblAddressLocality.visibility = GONE
		} else {
			lblAddressLocality.visibility = VISIBLE
			lblAddressLocality.text = text
		}
	}

	fun setPostalCode(text: String?) {
		lblAddressPostalCode.text = text
	}

	fun setRegion(text: String?) {
		if (text.isNullOrEmpty()) {
			lblAddressRegion.visibility = GONE
		} else {
			lblAddressRegion.visibility = VISIBLE
			lblAddressRegion.text = text
		}
	}

	fun setCountry(text: String?) {
		if (text.isNullOrEmpty()) {
			lblAddressCountry.visibility = GONE
		} else {
			lblAddressCountry.visibility = VISIBLE
			lblAddressCountry.text = text
		}
	}

	fun getMapButton() : MaterialButton {
		return btnShowOnMap
	}
}
