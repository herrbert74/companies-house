package com.babestudios.companyinfouk.ui.company

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.DaggerTestApplicationComponent
import com.babestudios.companyinfouk.TestApplicationComponent
import com.babestudios.companyinfouk.TestApplicationModule
import com.babestudios.companyinfo.data.model.company.Accounts
import com.babestudios.companyinfo.data.model.company.Company
import com.babestudios.companyinfo.data.model.company.LastAccounts
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class CompanyPresenterTest {

	private lateinit var companyPresenter: CompanyPresenter

	@Before
	fun setUp() {
		val testApplicationComponent :TestApplicationComponent = DaggerTestApplicationComponent.builder()
				.testApplicationModule(TestApplicationModule(CompaniesHouseApplication()))
				.build()
		companyPresenter = testApplicationComponent.companyPresenter()
		val company = Company()
		company.accounts = Accounts()
		company.accounts?.lastAccounts = LastAccounts()
		company.accounts?.lastAccounts?.type = "any"
		whenever(companyPresenter.companiesRepository.getCompany(any())).thenReturn(Single.just(company))
		val companyViewModel = CompanyViewModel()
		companyViewModel.state.value.companyNumber = "123"
		companyViewModel.state.value.companyName = "name"
		companyPresenter.setViewModel(companyViewModel, null)
	}

	@Test
	fun test_When_FabClicked_Then_DataManagerAddFavouriteIsCalled() {
		whenever(companyPresenter.companiesRepository.isFavourite(any())).thenReturn(false)
		companyPresenter.updateFavorites()
		verify(companyPresenter.companiesRepository).addFavourite(any())
	}

	@Test
	fun test_When_FabClicked_Then_DataManagerRemoveFavouriteIsCalled() {
		whenever(companyPresenter.companiesRepository.isFavourite(any())).thenReturn(true)
		companyPresenter.updateFavorites()
		verify(companyPresenter.companiesRepository).removeFavourite(any())
	}

	@Test
	fun test_When_GetCompany_Then_DataManagerGetCompanyIsCalled() {
		companyPresenter.fetchCompany("123456")
		verify(companyPresenter.companiesRepository).getCompany("123456")
	}
}
