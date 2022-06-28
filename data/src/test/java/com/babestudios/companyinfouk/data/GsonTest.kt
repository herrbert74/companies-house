package com.babestudios.companyinfouk.data

import com.babestudios.base.ext.loadJson
import com.babestudios.companyinfouk.data.BuildConfig.COMPANIES_HOUSE_BASE_URL
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Retrofit

class GsonTest {

	@Test
	fun advancedGsonConverter_searchCompanies_converts() {
		val advancedGsonConverterFactory = AdvancedGsonConverterFactory.create()
		val json = this.loadJson("search_result_you")
		val retrofit = Retrofit.Builder()//
				.baseUrl(COMPANIES_HOUSE_BASE_URL)//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.build()
		var response = CompanySearchResult()
		try {
			response = advancedGsonConverterFactory.responseBodyConverter(
					CompanySearchResult::class.java,
					null,
					retrofit
			)!!.convert(json.toResponseBody("application/json; charset=utf-8".toMediaType())) as CompanySearchResult
		} catch (e: Exception) {
			e.printStackTrace()
		}

		response.items[0].title?.shouldBeEqualComparingTo("YOU  LIMITED")
	}
}
