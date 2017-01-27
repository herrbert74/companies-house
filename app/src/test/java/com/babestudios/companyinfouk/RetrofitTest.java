package com.babestudios.companyinfouk;

import com.babestudios.companyinfouk.data.network.CompaniesHouseService;
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class RetrofitTest {

	private String authorization;


	@Before
	public void setUp() {
		authorization = "Basic WnBoWHBnLXRyZndBTmlUTmZlNHh3SzZRWFk0WHdSd3cwd0h4RjVkbQ==";
	}

	@Test
	public void testSearchCompanies() throws Exception {
		TestSubscriber<String> testSubscriber = new TestSubscriber<>();

		Retrofit r = new Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
				//.callbackExecutor(Executors.newSingleThreadExecutor())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.build();

		CompaniesHouseService companiesHouseService =  r.create(CompaniesHouseService.class);

		companiesHouseService.searchCompanies(authorization, "GAMES", "100", "0")
				.map(e -> e.items.get(0).title)
				.subscribe(testSubscriber);
		List<String> result = testSubscriber.getOnNextEvents();
		testSubscriber.assertValue("GAMES AGENCY LIMITED");
		assertThat(result.get(0).equals("GAMES AGENCY LIMITED"), is(true));
		testSubscriber.assertNoErrors();

	}
}
