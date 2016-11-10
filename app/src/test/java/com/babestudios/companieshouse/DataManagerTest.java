package com.babestudios.companieshouse;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.local.PreferencesHelper;
import com.babestudios.companieshouse.data.model.search.CompanySearchResult;
import com.babestudios.companieshouse.data.network.CompaniesHouseService;
import com.babestudios.companieshouse.utils.Base64Wrapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {
	@Mock
	CompaniesHouseService mockCompaniesHouseService;

	@Mock
	PreferencesHelper mockPreferencesHelper;

	@Mock
	Base64Wrapper base64Wrapper;

	private DataManager dataManager;

	@Rule
	public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

	@Before
	public void setUp() {
		dataManager = new DataManager(mockCompaniesHouseService, mockPreferencesHelper, base64Wrapper);
		//authorization = "Basic WnBoWHBnLXRyZndBTmlUTmZlNHh3SzZRWFk0WHdSd3cwd0h4RjVkbQ==";
	}

	@Test
	public void test_searchCompanies_successful(){
		CompanySearchResult companySearchResult = new CompanySearchResult();
		doReturn(Observable.just(companySearchResult))
				.when(mockCompaniesHouseService)
				.searchCompanies(anyString(), anyString(), anyString(), anyString());

		TestSubscriber<CompanySearchResult> testSubscriber = new TestSubscriber<>();
		dataManager.searchCompanies("Games", "0").subscribe(testSubscriber);
		//testSubscriber.assertValue(companySearchResult);
		testSubscriber.assertCompleted();
		testSubscriber.assertNoErrors();

	}
}
