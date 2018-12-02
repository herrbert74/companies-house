package com.babestudios.companyinfouk;

import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;
import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

import static com.babestudios.companyinfouk.BuildConfig.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class GsonTest {

	@Test
	public void advancedGsonConverter_map_converts() {
		AdvancedGsonConverterFactory c = AdvancedGsonConverterFactory.create();
		InputStream i = this.getClass().getClassLoader().getResourceAsStream("json_map.json");
		String z = "";
		try {
			z = new String(toByteArray(i), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//ResponseBody r = ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), z);
		Gson gson = new Gson();
		CapitalMap capitalMap = gson.fromJson(z, CapitalMap.class);
		assertThat(capitalMap.getCapital().size() == 2, is(true));
	}

	@Test
	public void single_item_map_converts() {
		AdvancedGsonConverterFactory c = AdvancedGsonConverterFactory.create();
		String z = "";
		try {
			InputStream i = this.getClass().getClassLoader().getResourceAsStream("json_map_single.json");
			z = new String(toByteArray(i), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ResponseBody r = ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), z);
		Gson gson = new Gson();
		CapitalMap capitalMap = gson.fromJson(z, CapitalMap.class);
		assertThat(capitalMap.getCapital().size() == 1, is(true));
	}

	public static byte[] toByteArray(InputStream stream) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] bytes = new byte[4048];
		int bytesRead = stream.read(bytes);
		while (bytesRead != -1) {
			output.write(bytes, 0, bytesRead);
			bytesRead = stream.read(bytes);
		}
		byte[] result = output.toByteArray();
		output.close();
		return result;
	}

	@Test
	public void advancedGsonConverter_searchCompanies_converts() {
		AdvancedGsonConverterFactory c = AdvancedGsonConverterFactory.create();
		ResponseBody r = ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), "{\n" +
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
				"}");

		Retrofit retrofit = new Retrofit.Builder()//
				.baseUrl(COMPANIES_HOUSE_BASE_URL)//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.build();
		CompanySearchResult response = new CompanySearchResult();
		try {
			response = ((CompanySearchResult) c.responseBodyConverter(CompanySearchResult.class, null, retrofit).convert(r));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(response.getItems().get(0).getTitle().equals("GAMES ALLIANCE LIMITED"), is(true));
	}
}
