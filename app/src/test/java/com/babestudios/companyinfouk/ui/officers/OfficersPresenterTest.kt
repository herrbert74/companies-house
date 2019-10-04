package com.babestudios.companyinfouk.ui.officers

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.DaggerTestApplicationComponent
import com.babestudios.companyinfouk.TestApplicationModule
import com.babestudios.companyinfo.data.model.officers.Officers
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.CompletableSource
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OfficersPresenterTest {

	private lateinit var officersPresenter: OfficersPresenter

	@Before
	fun setUp() {
		val testApplicationComponent = DaggerTestApplicationComponent.builder()
				.testApplicationModule(TestApplicationModule(CompaniesHouseApplication()))
				.build()
		officersPresenter = testApplicationComponent.officersPresenter()
		whenever(officersPresenter.companiesRepository.getOfficers("0", "0")).thenReturn(Single.just(Officers()))
		val officersViewModel = OfficersViewModel()
		officersViewModel.state.value.companyNumber = "0"
		officersPresenter.setViewModel(officersViewModel, CompletableSource { })

	}

	@Test
	fun whenGetInsolvency_thenDataManagerGetInsolvencyIsCalled() {
		officersPresenter.fetchOfficers("0")
		verify(officersPresenter.companiesRepository, times(2)).getOfficers("0", "0")
	}
}