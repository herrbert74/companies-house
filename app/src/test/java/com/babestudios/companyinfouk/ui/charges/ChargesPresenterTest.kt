package com.babestudios.companyinfouk.ui.charges

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.DaggerTestApplicationComponent
import com.babestudios.companyinfouk.TestApplicationModule
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ChargesPresenterTest {

	private lateinit var chargesPresenter: ChargesPresenter

	@Before
	fun setUp() {
		val testApplicationComponent = DaggerTestApplicationComponent.builder()
				.testApplicationModule(TestApplicationModule(CompaniesHouseApplication()))
				.build()
		chargesPresenter = testApplicationComponent.chargesPresenter()
		whenever(chargesPresenter.companiesRepository.fetchCharges("123", "0")).thenReturn(Single.just(Charges()))
		val chargesViewModel = ChargesViewModel()
		chargesViewModel.state.value.companyNumber = "123"
		chargesPresenter.setViewModel(chargesViewModel, Completable.fromCallable { false })
	}

	@Test
	fun whenGetInsolvency_thenDataManagerGetInsolvencyIsCalled() {
		chargesPresenter.fetchCharges("123")
		verify<CompaniesRepositoryContract>(chargesPresenter.companiesRepository, times(2)).fetchCharges("123", "0")
	}

	@Test
	fun whenLoadMoreCharges_thenDataManagerLoadMoreChargesIsCalled() {
		chargesPresenter.loadMoreCharges(0)
		verify<CompaniesRepositoryContract>(chargesPresenter.companiesRepository, times(2)).fetchCharges("123", "0")
	}
}