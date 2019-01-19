package com.babestudios.companyinfouk.ui.company

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.company.Accounts
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.data.model.company.LastAccounts
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CompanyPresenterTest {

	private lateinit var companyPresenter: CompanyPresenter

	@Before
	fun setUp() {
		val mockCompaniesRepository: CompaniesRepository = mock()
		val view: CompanyActivityView = mock()
		companyPresenter = CompanyPresenter(mockCompaniesRepository)
		companyPresenter.create()
		val company = Company()
		company.accounts = Accounts()
		company.accounts?.lastAccounts = LastAccounts()
		company.accounts?.lastAccounts?.type = "any"
		whenever(view.companyName).thenReturn("name")
		whenever(view.companyNumber).thenReturn("123")
		whenever(companyPresenter.companiesRepository.getCompany(any())).thenReturn(Observable.just(company))
		whenever(companyPresenter.companiesRepository.accountTypeLookup(any())).thenReturn("")
		companyPresenter.attachView(view)
	}

	@Test
	fun test_When_FabClicked_Then_DataManagerAddFavouriteIsCalled() {
		whenever(companyPresenter.companiesRepository.isFavourite(any())).thenReturn(false)
		companyPresenter.observablesFromViews(Observable.just(1))
		verify(companyPresenter.companiesRepository).addFavourite(any())
	}

	@Test
	fun test_When_FabClicked_Then_DataManagerRemoveFavouriteIsCalled() {
		whenever(companyPresenter.companiesRepository.isFavourite(any())).thenReturn(true)
		companyPresenter.observablesFromViews(Observable.just(1))
		verify(companyPresenter.companiesRepository).removeFavourite(any())
	}

	@Test
	fun test_When_GetCompany_Then_DataManagerGetCompanyIsCalled() {
		companyPresenter.getCompany("123456")
		verify(companyPresenter.companiesRepository).getCompany("123456")
	}
}
