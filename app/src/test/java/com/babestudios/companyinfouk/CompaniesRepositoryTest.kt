package com.babestudios.companyinfouk

import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.utils.Base64Wrapper
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class CompaniesRepositoryTest {

	@Mock
	internal var mockCompaniesHouseService: CompaniesHouseService? = null

	@Mock
	internal var mockCompaniesHouseDocumentService: CompaniesHouseDocumentService? = null

	@Mock
	internal var mockPreferencesHelper: PreferencesHelper? = null

	@Mock
	internal var base64Wrapper: Base64Wrapper? = null

	@Mock
	internal  var constantsHelper: ConstantsHelper? = null
	@Mock
	internal var filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelper? = null

	private var companiesRepository: CompaniesRepository? = null

	private lateinit var companySearchResult: CompanySearchResult

	private val immediate = object : Scheduler() {
		override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
			// this prevents StackOverflowErrors when scheduling with a delay
			return super.scheduleDirect(run, 0, unit)
		}

		override fun createWorker(): Worker {
			return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
		}
	}

	@Before
	fun setUp() {
		companiesRepository = CompaniesRepository(mockCompaniesHouseService!!, mockCompaniesHouseDocumentService!!, mockPreferencesHelper!!, base64Wrapper!!, constantsHelper!!, filingHistoryDescriptionsHelper!!)
		//authorization = "Basic WnBoWHBnLXRyZndBTmlUTmZlNHh3SzZRWFk0WHdSd3cwd0h4RjVkbQ==";
		companySearchResult = CompanySearchResult()
		doReturn(Single.just(companySearchResult))
				.`when`<CompaniesHouseService>(mockCompaniesHouseService)
				.searchCompanies(anyString(), anyString(), anyString(), anyString())

		RxJavaPlugins.setInitIoSchedulerHandler { immediate }
		RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
		RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
		RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
		RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
	}

	@Test
	fun test_searchCompanies_successful() {
		val testSubscriber = TestObserver<CompanySearchResult>()
		companiesRepository?.searchCompanies("Games", "0")?.subscribe(testSubscriber)
		//testSubscriber.assertTerminated()
		//testSubscriber.assertValue(companySearchResult)
		//testSubscriber.assertComplete()
		testSubscriber.assertNoErrors()
	}
}
