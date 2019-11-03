package com.babestudios.companyinfouk.ui.filinghistory

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.DaggerTestApplicationComponent
import com.babestudios.companyinfouk.TestApplicationModule
import com.babestudios.companyinfouk.common.model.filinghistory.CategoryDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistory
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FilingHistoryPresenterTest {

	private lateinit var filingHistoryPresenter: FilingHistoryPresenter

	@Before
	fun setUp() {
		val testApplicationComponent = DaggerTestApplicationComponent.builder()
				.testApplicationModule(TestApplicationModule(CompaniesHouseApplication()))
				.build()
		filingHistoryPresenter = testApplicationComponent.filingHistoryPresenter()
		val viewModel = FilingHistoryViewModel()
		val completable: CompletableSource = Completable.fromCallable { "" }
		filingHistoryPresenter.setViewModel(viewModel, completable)
		`when`(filingHistoryPresenter.companiesRepository.getFilingHistory("23", "", "0")).thenReturn(Single.just(FilingHistory()))
	}

	@Test
	fun whenGetFilingHistory_thenDataManagerGetFilingHistoryIsCalled() {
		filingHistoryPresenter.getFilingHistory("23", CategoryDto.CATEGORY_SHOW_ALL)
		verify(filingHistoryPresenter.companiesRepository, times(1)).getFilingHistory("23", "", "0")
	}

}
