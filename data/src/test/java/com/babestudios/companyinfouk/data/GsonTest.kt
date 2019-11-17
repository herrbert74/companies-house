package com.babestudios.companyinfouk.data

import com.babestudios.companyinfouk.data.BuildConfig.COMPANIES_HOUSE_BASE_URL
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.google.gson.Gson

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

@RunWith(JUnit4::class)
class GsonTest {

	@Test
	fun advancedGsonConverter_map_converts() {
		val stream = this.javaClass.classLoader!!.getResourceAsStream("json_map.json")
		var json = ""
		try {
			json = String(toByteArray(stream), StandardCharsets.UTF_8)
		} catch (e: IOException) {
			e.printStackTrace()
		}

		val gson = Gson()
		val capitalMap = gson.fromJson(json, CapitalMap::class.java)
		assertThat(capitalMap.capital.size == 2, `is`(true))
	}

	@Test
	fun single_item_map_converts() {
		var json = ""
		try {
			val i = this.javaClass.classLoader!!.getResourceAsStream("json_map_single.json")
			json = String(toByteArray(i), StandardCharsets.UTF_8)
		} catch (e: Exception) {
			e.printStackTrace()
		}
		val gson = Gson()
		val capitalMap = gson.fromJson(json, CapitalMap::class.java)
		assertThat(capitalMap.capital.size == 1, `is`(true))
	}

	@Test
	fun advancedGsonConverter_searchCompanies_converts() {
		val advancedGsonConverterFactory = AdvancedGsonConverterFactory.create()
		var json = ""
		try {
			val i = this.javaClass.classLoader!!.getResourceAsStream("search_result_you.json")
			json = String(toByteArray(i), StandardCharsets.UTF_8)
		} catch (e: Exception) {
			e.printStackTrace()
		}
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

	companion object {

		@Throws(IOException::class)
		fun toByteArray(stream: InputStream): ByteArray {
			val output = ByteArrayOutputStream()
			val bytes = ByteArray(4048)
			var bytesRead = stream.read(bytes)
			while (bytesRead != -1) {
				output.write(bytes, 0, bytesRead)
				bytesRead = stream.read(bytes)
			}
			val result = output.toByteArray()
			output.close()
			return result
		}
	}
}
