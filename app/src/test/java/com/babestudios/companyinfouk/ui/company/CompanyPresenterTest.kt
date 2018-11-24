package com.babestudios.companyinfouk.ui.company

import com.babestudios.companyinfouk.data.DataManager
import com.babestudios.companyinfouk.data.model.company.Accounts
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.data.model.company.LastAccounts
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CompanyPresenterTest {

	private lateinit var companyPresenter: CompanyPresenter

	@Before
	fun setUp() {
		val mockDataManager: DataManager = mock()
		val view: CompanyActivityView = mock()
		companyPresenter = CompanyPresenter(mockDataManager)
		companyPresenter.create()
		val company = Company()
		company.accounts = Accounts()
		company.accounts.lastAccounts = LastAccounts()
		company.accounts.lastAccounts.type = "any"
		`when`(companyPresenter.dataManager.getCompany(any())).thenReturn(Observable.just(company))
		`when`(companyPresenter.dataManager.accountTypeLookup(any())).thenReturn("")
		companyPresenter.attachView(view)
	}

	@Test
	fun test_When_FabClicked_Then_DataManagerAddFavouriteIsCalled() {
		`when`(companyPresenter.dataManager.isFavourite(any())).thenReturn(false)
		companyPresenter.observablesFromViews(Observable.just(1))
		verify(companyPresenter.dataManager).addFavourite(any())
	}

	@Test
	fun test_When_FabClicked_Then_DataManagerRemoveFavouriteIsCalled() {
		`when`(companyPresenter.dataManager.isFavourite(any())).thenReturn(true)
		companyPresenter.observablesFromViews(Observable.just(1))
		verify(companyPresenter.dataManager).removeFavourite(any())
	}

	@Test
	fun test_When_GetCompany_Then_DataManagerGetCompanyIsCalled() {
		companyPresenter.getCompany("123456")
		verify(companyPresenter.dataManager).getCompany("123456")
	}
}
