package com.babestudios.companyinfouk.companies.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.existingViewModel
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel

class PrivacyFragment : BaseMvRxFragment() {

	private val viewModel by existingViewModel(CompaniesViewModel::class)

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		val view = WebView(requireContext())
		view.settings.javaScriptEnabled = true
		view.loadUrl("file:///android_asset/privacy_policy.html")
		return view

	}

	@Suppress("EmptyFunctionBlock")
	override fun invalidate() {

	}
}
