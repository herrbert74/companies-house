package com.babestudios.companieshouse;

import android.util.Base64;

import com.babestudios.companieshouse.network.SearchCompaniesService;
import com.babestudios.companieshouse.network.converters.AdvancedGsonConverterFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.observers.TestSubscriber;

@RunWith(JUnit4.class)
public class RetrofitTest {

	private String authorization;

	@Before
	public void setUp() {
		authorization =
				"Basic " + Base64.encodeToString(BuildConfig.COMPANIES_HOUSE_API_KEY.getBytes(), Base64.NO_WRAP);
	}

	@Test
	public void testGetEarningsDates() throws Exception {
		TestSubscriber<String> testSubscriber = new TestSubscriber<>();

		Retrofit r = new Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.build();

		SearchCompaniesService searchCompaniesService =  r.create(SearchCompaniesService.class);

		searchCompaniesService.searchCompanies(authorization, "GAMES", "100", "0")
				.map(e -> e.items.get(0).title)
				.subscribe(testSubscriber);
		testSubscriber.assertValue("GAMES ALLIANCE LIMITED");
		testSubscriber.assertNoErrors();

	}
}
