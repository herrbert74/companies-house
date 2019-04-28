package com.babestudios.companyinfouk.ui.insolvency

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.DaggerTestApplicationComponent
import com.babestudios.companyinfouk.TestApplicationModule
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.CompletableSource
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InsolvencyPresenterTest {

	private lateinit var insolvencyPresenter: InsolvencyPresenter

	@Before
	fun setUp() {
		val testApplicationComponent = DaggerTestApplicationComponent.builder()
				.testApplicationModule(TestApplicationModule(CompaniesHouseApplication()))
				.build()
		insolvencyPresenter = testApplicationComponent.insolvencyPresenter()
		whenever(insolvencyPresenter.companiesRepository.getInsolvency("0")).thenReturn(Single.just(Insolvency()))
		val insolvencyViewModel = InsolvencyViewModel()
		insolvencyViewModel.state.value.companyNumber = "0"
		insolvencyPresenter.setViewModel(insolvencyViewModel, CompletableSource { })
	}

	@Test
	fun test_When_GetInsolvency_Then_DataManagerGetInsolvencyIsCalled() {
		insolvencyPresenter.fetchInsolvencies("0")
		verify(insolvencyPresenter.companiesRepository, times(2)).getInsolvency("0")
	}
}