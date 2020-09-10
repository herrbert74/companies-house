package com.babestudios.companyinfouk.data

import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.BuildConfig.COMPANIES_HOUSE_BASE_URL
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.google.gson.Gson
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Retrofit

class GsonTest {

	@Test
	fun advancedGsonConverter_map_converts() {
		val json = this.loadJson("json_map")
		val capitalMap = Gson().fromJson(json, CapitalMap::class.java)
		capitalMap.capital.size shouldBeEqualComparingTo  2
	}

	@Test
	fun single_item_map_converts() {
		val json = this.loadJson("json_map_single")
		val gson = Gson()
		val capitalMap = gson.fromJson(json, CapitalMap::class.java)
		capitalMap.capital.size shouldBeEqualComparingTo  1
	}

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
