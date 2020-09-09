package com.babestudios.companyinfouk.data

import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.BuildConfig.COMPANIES_HOUSE_BASE_URL
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import retrofit2.Retrofit

class GsonTest {

	@Test
	fun advancedGsonConverter_map_converts() {
		val json = this.loadJson("json_map")
		val capitalMap = Gson().fromJson(json, CapitalMap::class.java)
		assertThat(capitalMap.capital.size == 2, `is`(true))
	}

	@Test
	fun single_item_map_converts() {
		val json = this.loadJson("json_map_single")
		val gson = Gson()
		val capitalMap = gson.fromJson(json, CapitalMap::class.java)
		assertThat(capitalMap.capital.size == 1, `is`(true))
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

		assertThat(response.items[0].title == "YOU  LIMITED", `is`(true))
	}
}
