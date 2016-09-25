package com.babestudios.companieshouse;

import com.babestudios.companieshouse.data.network.converters.AdvancedGsonConverterFactory;
import com.babestudios.companieshouse.data.model.CompanySearchResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

import static com.babestudios.companieshouse.BuildConfig.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class GsonTest {

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
				"      \"links\": {\n" +
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
		assertThat(response.items.get(0).title.equals("GAMES ALLIANCE LIMITED"), is(true));
	}
}
