package com.babestudios.companyinfouk.ui.filinghistory

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.filinghistory.Category
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList
import io.reactivex.*
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class FilingHistoryPresenterTest {

	private lateinit var filingHistoryPresenter: FilingHistoryPresenter

	@Before
	fun setUp() {
		RxJavaPlugins.setInitIoSchedulerHandler { immediate }
		RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
		RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
		RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
		RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
		val viewModel = FilingHistoryViewModel()
		val completable: CompletableSource = Completable.fromCallable { "" }
		val dataManager = mock(CompaniesRepository::class.java)
		filingHistoryPresenter = FilingHistoryPresenter(dataManager)
		filingHistoryPresenter.setViewModel(viewModel, completable)
		`when`(filingHistoryPresenter.companiesRepository.getFilingHistory("23", "", "0")).thenReturn(Single.just(FilingHistoryList()))
	}

	@Test
	fun whenGetFilingHistory_thenDataManagerGetFilingHistoryIsCalled() {
		filingHistoryPresenter.getFilingHistory("23", Category.CATEGORY_SHOW_ALL)
		verify(filingHistoryPresenter.companiesRepository, times(1)).getFilingHistory("23", "", "0")
	}

	private val immediate = object : Scheduler() {
		override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
			// this prevents StackOverflowErrors when scheduling with a delay
			return super.scheduleDirect(run, 0, unit)
		}

		override fun createWorker(): Scheduler.Worker {
			return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
		}
	}
}
