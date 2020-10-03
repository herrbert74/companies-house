package com.babestudios.companyinfouk.companies.ui.privacy

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.airbnb.mvrx.existingViewModel
import com.babestudios.base.mvrx.BaseFragment
import com.babestudios.companyinfouk.companies.ui.CompaniesActivity
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel

class PrivacyFragment : BaseFragment() {

	private val viewModel by existingViewModel(CompaniesViewModel::class)

	@SuppressLint("SetJavaScriptEnabled")
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

	override fun orientationChanged() {
		val activity = requireActivity() as CompaniesActivity
		viewModel.setNavigator(activity.injectCompaniesNavigator())
	}

	@Suppress("EmptyFunctionBlock")
	override fun invalidate() {

	}
}
