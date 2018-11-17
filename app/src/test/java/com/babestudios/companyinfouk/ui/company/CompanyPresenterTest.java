package com.babestudios.companyinfouk.ui.company;

import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.company.Accounts;
import com.babestudios.companyinfouk.data.model.company.Company;
import com.babestudios.companyinfouk.data.model.company.LastAccounts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompanyPresenterTest {

	private CompanyPresenter companyPresenter;

	@Before
	public void setUp() {
		companyPresenter = new CompanyPresenter(mock(DataManager.class));
		companyPresenter.create();
		CompanyActivityView view = mock(CompanyActivityView.class);
		Company company = new Company();
		company.accounts = new Accounts();
		company.accounts.lastAccounts = new LastAccounts();
		company.accounts.lastAccounts.type = "any";
		when(companyPresenter.getDataManager().getCompany(any())).thenReturn(Observable.just(company));
		when(companyPresenter.getDataManager().accountTypeLookup(any())).thenReturn("");
		companyPresenter.attachView(view);
	}

	@Test
	public void test_When_FabClicked_Then_DataManagerAddFavouriteIsCalled() {
		when(companyPresenter.getDataManager().isFavourite(any())).thenReturn(false);
		companyPresenter.observablesFromViews(Observable.just(1));
		verify(companyPresenter.getDataManager()).addFavourite(any());
	}

	@Test
	public void test_When_FabClicked_Then_DataManagerRemoveFavouriteIsCalled() {
		when(companyPresenter.getDataManager().isFavourite(any())).thenReturn(true);
		companyPresenter.observablesFromViews(Observable.just(1));
		verify(companyPresenter.getDataManager()).removeFavourite(any());
	}

	@Test
	public void test_When_GetCompany_Then_DataManagerGetCompanyIsCalled() {
		companyPresenter.getCompany("123456");
		verify(companyPresenter.getDataManager()).getCompany("123456");
	}
}
