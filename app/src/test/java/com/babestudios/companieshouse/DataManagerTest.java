package com.babestudios.companieshouse;

import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.CompanySearchResult;
import com.babestudios.companieshouse.data.network.CompaniesHouseService;
import com.babestudios.companieshouse.ui.search.SearchPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {
	@Mock
	CompaniesHouseService mockCompaniesHouseService;
	private DataManager dataManager;
	private String authorization;

	@Rule
	public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

	@Before
	public void setUp() {
		dataManager = new DataManager(mockCompaniesHouseService);
		authorization = "Basic WnBoWHBnLXRyZndBTmlUTmZlNHh3SzZRWFk0WHdSd3cwd0h4RjVkbQ==";
	}

	@Test
	public void searchCompanies_successful(){
		CompanySearchResult companySearchResult = new CompanySearchResult();
		doReturn(Observable.just(companySearchResult))
				.when(mockCompaniesHouseService)
				.searchCompanies(anyString(), anyString(), anyString(), anyString());

		TestSubscriber<CompanySearchResult> testSubscriber = new TestSubscriber<>();
		dataManager.searchCompanies(authorization, "Games", "0").subscribe(testSubscriber);
		testSubscriber.assertValue(companySearchResult);
		testSubscriber.assertCompleted();
		testSubscriber.assertNoErrors();

	}
}
