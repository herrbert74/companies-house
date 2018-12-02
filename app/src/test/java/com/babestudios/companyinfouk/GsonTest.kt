package com.babestudios.companyinfouk

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

import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Retrofit

import com.babestudios.companyinfouk.BuildConfig.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

@RunWith(JUnit4::class)
class GsonTest {

	@Test
	fun advancedGsonConverter_map_converts() {
		val c = AdvancedGsonConverterFactory.create()
		val i = this.javaClass.classLoader!!.getResourceAsStream("json_map.json")
		var z = ""
		try {
			z = String(toByteArray(i), StandardCharsets.UTF_8)
		} catch (e: IOException) {
			e.printStackTrace()
		}

		//ResponseBody r = ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), z);
		val gson = Gson()
		val capitalMap = gson.fromJson(z, CapitalMap::class.java)
		assertThat(capitalMap.capital.size == 2, `is`(true))
	}

	@Test
	fun single_item_map_converts() {
		val c = AdvancedGsonConverterFactory.create()
		var z = ""
		try {
			val i = this.javaClass.classLoader!!.getResourceAsStream("json_map_single.json")
			z = String(toByteArray(i), StandardCharsets.UTF_8)
		} catch (e: Exception) {
			e.printStackTrace()
		}

		//ResponseBody r = ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), z);
		val gson = Gson()
		val capitalMap = gson.fromJson(z, CapitalMap::class.java)
		assertThat(capitalMap.capital.size == 1, `is`(true))
	}

	@Test
	fun advancedGsonConverter_searchCompanies_converts() {
		val c = AdvancedGsonConverterFactory.create()
		val r = ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), "{\n" +
				"  \"total_results\": 4706,\n" +
				"  \"items_per_page\": 1,\n" +
				"  \"page_number\": 2,\n" +
				"  \"kind\": \"search#companies\",\n" +
				"  \"start_index\": 1,\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"address_snippet\": \"Unit C Nepshaw Lane South, Gildersome, Morley, Leeds, England, LS27 7JQ\",\n" +
				"      \"company_number\": \"09802398\",\n" +
				"      \"companyLinks\": {\n" +
				"        \"self\": \"/company/09802398\"\n" +
				"      },\n" +
				"      \"date_of_creation\": \"2015-09-30\",\n" +
				"      \"kind\": \"searchresults#company\",\n" +
				"      \"snippet\": \"\",\n" +
				"      \"matches\": {\n" +
				"        \"snippet\": [],\n" +
				"        \"title\": [\n" +
				"          1,\n" +
				"          5\n" +
				"        ]\n" +
				"      },\n" +
				"      \"address\": {\n" +
				"        \"premises\": \"Unit C Nepshaw Lane South, Gildersome\",\n" +
				"        \"country\": \"England\",\n" +
				"        \"postal_code\": \"LS27 7JQ\",\n" +
				"        \"address_line_1\": \"Morley\",\n" +
				"        \"locality\": \"Leeds\"\n" +
				"      },\n" +
				"      \"title\": \"GAMES ALLIANCE LIMITED\",\n" +
				"      \"company_type\": \"ltd\",\n" +
				"      \"company_status\": \"active\",\n" +
				"      \"description\": \"09802398 - Incorporated on 30 September 2015\",\n" +
				"      \"description_identifier\": [\n" +
				"        \"incorporated-on\"\n" +
				"      ]\n" +
				"    }\n" +
				"  ]\n" +
				"}")

		val retrofit = Retrofit.Builder()//
				.baseUrl(COMPANIES_HOUSE_BASE_URL)//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.build()
		var response = CompanySearchResult()
		try {
			response = c.responseBodyConverter(CompanySearchResult::class.java, null, retrofit)!!.convert(r) as CompanySearchResult
		} catch (e: Exception) {
			e.printStackTrace()
		}

		assertThat(response.items[0].title == "GAMES ALLIANCE LIMITED", `is`(true))
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
