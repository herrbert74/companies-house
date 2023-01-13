package com.babestudios.companyinfouk.companies.ui.privacy

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PrivacyFragment : Fragment() {

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	@SuppressLint("SetJavaScriptEnabled")
	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		companiesRepository.logScreenView(this::class.simpleName.orEmpty())
		val view = WebView(requireContext())
		view.settings.javaScriptEnabled = true
		view.loadUrl("file:///android_asset/privacy_policy.html")
		return view

	}

}
