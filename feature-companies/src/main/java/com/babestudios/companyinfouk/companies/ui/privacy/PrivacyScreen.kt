package com.babestudios.companyinfouk.companies.ui.privacy

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.shared.screen.privacy.PrivacyComp
import com.babestudios.companyinfouk.shared.screen.privacy.PrivacyComponent
import kotlinx.coroutines.Dispatchers

@SuppressLint("SetJavaScriptEnabled")
@Composable
@Suppress("LongMethod", "ComplexMethod")
fun PrivacyScreen(
	component: PrivacyComp,
	modifier: Modifier = Modifier,
) {

	val topAppBarColors = TopAppBarDefaults.topAppBarColors(
		containerColor = MaterialTheme.colorScheme.primaryContainer,
	)

	BackHandler(onBack = { component.onBackClicked() })

	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				colors = topAppBarColors,
				title = { Text(stringResource(R.string.privacy_policy)) },
				navigationIcon = {
					IconButton(onClick = { component.onBackClicked() }) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = "Finish",
						)
					}
				},
			)
		}
	) { paddingValues ->

		AndroidView(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues),
			factory = { context ->
				WebView(context).apply {
					settings.javaScriptEnabled = true
					layoutParams = ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT
					)
					webChromeClient = WebChromeClient()
					loadUrl("file:///android_asset/privacy_policy.html")
				}

			}
		)

	}

}

@Preview("privacy Preview")
@Composable
private fun PrivacyScreenPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	CompaniesTheme {
		PrivacyScreen(
			PrivacyComponent(
				componentContext,
				Dispatchers.Main,
			) { }
		)
	}
}
