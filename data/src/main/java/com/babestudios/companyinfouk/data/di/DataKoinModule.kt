package com.babestudios.companyinfouk.data.di

import android.content.Context
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesAccessor
import com.babestudios.companyinfouk.data.CompaniesDocumentAccessor
import com.babestudios.companyinfouk.data.local.PREF_FILE_NAME
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.shared.data.network.CompaniesHouseApi
import com.babestudios.companyinfouk.shared.data.network.CompaniesHouseDocumentApi
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.encodeBase64
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.lighthousegames.logging.logging

private const val NETWORK_TIMEOUT = 30_000L

val dataModule = module {

	//region Repository

	single {
		CompaniesAccessor(
			get(named("CompaniesHouseApi")),
			get(),
			get(),
			get(named("IoDispatcher")),
		)
	}.withOptions {
		bind<CompaniesRepository>()
	}

	single {
		CompaniesDocumentAccessor(
			androidContext(),
			get(named("CompaniesHouseDocumentApi")),
			get(named("IoDispatcher")),
		)
	}.withOptions {
		bind<CompaniesDocumentRepository>()
	}

	//endregion

	single { androidContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE) }

	single { FirebaseAnalytics.getInstance(androidContext()) }

	single {
		Json {
			prettyPrint = true
			isLenient = true
			ignoreUnknownKeys = true
		}
	}

	single { PreferencesHelper(get(), get()) }

	single {
		ChuckerInterceptor.Builder(androidContext())
			.collector(ChuckerCollector(androidContext(), showNotification = false))
			.build()
	}

	single {
		OkHttp.create {
			addInterceptor(get<ChuckerInterceptor>())
		}
	}

	single(named("CompaniesHouseClient")) {
		HttpClient(get()) {
			install(HttpTimeout) { requestTimeoutMillis = NETWORK_TIMEOUT }
			install(customHeaderPlugin)
			install(ContentNegotiation) { json(get()) }
			install(Logging) {
				logger = object : Logger {
					override fun log(message: String) {
						logging().d { "AA-430 log: $message" }
					}
				}
				level = LogLevel.ALL
			}
			defaultRequest {
				host = "api.companieshouse.gov.uk"
				url {
					protocol = URLProtocol.HTTPS
				}
			}
		}
	}

	single(named("CompaniesHouseDocumentClient")) {
		HttpClient(get()) {
			install(HttpTimeout) { requestTimeoutMillis = NETWORK_TIMEOUT }
			install(customHeaderPlugin)
			install(ContentNegotiation) { json(get()) }
			install(Logging) {
				logger = object : Logger {
					override fun log(message: String) {
						logging().d { "AA-430 log: $message" }
					}
				}
				level = LogLevel.ALL
			}
			defaultRequest {
				host = "document-api.companieshouse.gov.uk"
				url {
					protocol = URLProtocol.HTTPS
				}
			}
		}
	}

	single(named("CompaniesHouseApi")) {
		val ktorfit = Ktorfit.Builder().httpClient(get<HttpClient>(named("CompaniesHouseClient"))).build()
		ktorfit.create<CompaniesHouseApi>()
	}

	single(named("CompaniesHouseDocumentApi")) {
		val ktorfit = Ktorfit.Builder().httpClient(get<HttpClient>(named("CompaniesHouseDocumentClient"))).build()
		ktorfit.create<CompaniesHouseDocumentApi>()
	}

}

val customHeaderPlugin = createClientPlugin("CustomHeaderPlugin") {
	onRequest { request, _ ->
		request.headers.append(
			"Authorization",
			"Basic ${BuildConfig.COMPANIES_HOUSE_API_KEY.toByteArray().encodeBase64()}"
		)
	}
}
