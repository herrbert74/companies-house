package com.babestudios.companyinfouk;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.local.PreferencesHelper;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService;
import com.babestudios.companyinfouk.data.network.CompaniesHouseService;
import com.babestudios.companyinfouk.utils.Base64Wrapper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

	@Mock
	CompaniesHouseService mockCompaniesHouseService;

	@Mock
	CompaniesHouseDocumentService mockCompaniesHouseDocumentService;

	@Mock
	PreferencesHelper mockPreferencesHelper;

	@Mock
	Base64Wrapper base64Wrapper;

	private DataManager dataManager;

	//@Rule
	//public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();
	private CompanySearchResult companySearchResult;

	@Before
	public void setUp() {
		dataManager = new DataManager(mockCompaniesHouseService, mockCompaniesHouseDocumentService, mockPreferencesHelper, base64Wrapper);
		//authorization = "Basic WnBoWHBnLXRyZndBTmlUTmZlNHh3SzZRWFk0WHdSd3cwd0h4RjVkbQ==";
		companySearchResult = new CompanySearchResult();
		doReturn(Observable.just(companySearchResult))
				.when(mockCompaniesHouseService)
				.searchCompanies(anyString(), anyString(), anyString(), anyString());
	}

	@Test
	public void test_searchCompanies_successful(){


		//RxJavaHooks.setOnGenericScheduledExecutorService(Schedulers.immediate());

		TestObserver<CompanySearchResult> testSubscriber = new TestObserver<>();
		dataManager.searchCompanies("Games", "0").subscribe(testSubscriber);
		testSubscriber.assertTerminated();
		testSubscriber.assertValue(companySearchResult);
		testSubscriber.assertComplete();
		testSubscriber.assertNoErrors();

	}
}
