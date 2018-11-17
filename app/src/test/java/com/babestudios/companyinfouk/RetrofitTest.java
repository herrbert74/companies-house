package com.babestudios.companyinfouk;

import com.babestudios.companyinfouk.data.network.CompaniesHouseService;
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import io.reactivex.observers.TestObserver;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

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
		TestObserver<String> testSubscriber = new TestObserver<>();

		Retrofit r = new Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
				//.callbackExecutor(Executors.newSingleThreadExecutor())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.build();

		CompaniesHouseService companiesHouseService =  r.create(CompaniesHouseService.class);

		companiesHouseService.searchCompanies(authorization, "GAMES", "100", "0")
				.map(e -> e.items.get(0).title)
				.subscribe(testSubscriber);
		List<Object> result = testSubscriber.getEvents().get(0);
		testSubscriber.assertValue("CELESTIAL GAMES & BOOKS LTD");//GAMES AGENCY LIMITED");
		assertThat(result.get(0).equals("CELESTIAL GAMES & BOOKS LTD"), is(true));//GAMES AGENCY LIMITED")
		testSubscriber.assertNoErrors();

	}
}
