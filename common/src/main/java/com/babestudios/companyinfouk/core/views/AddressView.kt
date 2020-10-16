package com.babestudios.companyinfouk.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.babestudios.companyinfouk.common.R
import com.babestudios.companyinfouk.common.databinding.ViewAddressBinding
import com.google.android.material.button.MaterialButton


@Suppress("unused")
class AddressView @JvmOverloads constructor(
		context: Context,
		attrs: AttributeSet? = null,
		defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

	private var _binding: ViewAddressBinding? = null
	private val binding get() = _binding!!

	init {
		_binding = ViewAddressBinding.inflate(LayoutInflater.from(context), this)
		obtainStyleAttributes(context, attrs)
	}

	private fun obtainStyleAttributes(context: Context, attrs: AttributeSet?) {
		context.theme.obtainStyledAttributes(attrs, R.styleable.AddressView, 0, 0).apply {
			binding.cpnAddressTitle.text = getString(R.styleable.AddressView_title)
			binding.lblAddressAddressLine1.text = getString(R.styleable.AddressView_addressLine1)
			binding.lblAddressAddressLine2.text = getString(R.styleable.AddressView_addressLine2)
			binding.lblAddressLocality.text = getString(R.styleable.AddressView_locality)
			binding.lblAddressPostalCode.text = getString(R.styleable.AddressView_postCode)
			binding.lblAddressRegion.text = getString(R.styleable.AddressView_region)
			binding.lblAddressCountry.text = getString(R.styleable.AddressView_country)
		}.recycle()
	}

	fun setTitle(text: String?) {
		binding.cpnAddressTitle.text = text
	}

	fun setAddressLine1(text: String?) {
		binding.lblAddressAddressLine1.text = text
	}

	fun setAddressLine2(text: String?) {
		if (text.isNullOrEmpty()) {
			binding.lblAddressAddressLine2.visibility = GONE
		} else {
			binding.lblAddressAddressLine2.visibility = VISIBLE
			binding.lblAddressAddressLine2.text = text
		}
	}

	fun setLocality(text: String?) {
		if (text.isNullOrEmpty()) {
			binding.lblAddressLocality.visibility = GONE
		} else {
			binding.lblAddressLocality.visibility = VISIBLE
			binding.lblAddressLocality.text = text
		}
	}

	fun setPostalCode(text: String?) {
		binding.lblAddressPostalCode.text = text
	}

	fun setRegion(text: String?) {
		if (text.isNullOrEmpty()) {
			binding.lblAddressRegion.visibility = GONE
		} else {
			binding.lblAddressRegion.visibility = VISIBLE
			binding.lblAddressRegion.text = text
		}
	}

	fun setCountry(text: String?) {
		if (text.isNullOrEmpty()) {
			binding.lblAddressCountry.visibility = GONE
		} else {
			binding.lblAddressCountry.visibility = VISIBLE
			binding.lblAddressCountry.text = text
		}
	}

	fun getMapButton() : MaterialButton {
		return binding.btnShowOnMap
	}
}
