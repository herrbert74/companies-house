package com.babestudios.companyinfouk.data

import android.content.Context
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.utils.Base64Wrapper
import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class CompaniesRepositoryTest {

	private val context = mockk<Context>()

	private val mockCompaniesHouseService = mockk<CompaniesHouseService>()

	private val mockCompaniesHouseDocumentService = mockk<CompaniesHouseDocumentService>()

	private val mockPreferencesHelper = mockk<PreferencesHelper>()

	private val base64Wrapper = mockk<Base64Wrapper>()

	private val firebaseAnalytics = mockk<FirebaseAnalytics>()

	private val constantsHelper = mockk<ConstantsHelper>()

	private val filingHistoryDescriptionsHelper = mockk<FilingHistoryDescriptionsHelper>()

	private val companiesRepository = mockk<CompaniesAccessor>()

	private val companySearchResult = mockk<CompanySearchResult>()

	private val immediate = object : Scheduler() {
		override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
			// this prevents StackOverflowErrors when scheduling with a delay
			return super.scheduleDirect(run, 0, unit)
		}

		override fun createWorker(): Worker {
			return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
		}
	}

	@Before
	fun setUp() {
		/*companiesRepository = CompaniesRepository(
				context!!,
				mockCompaniesHouseService!!,
				mockCompaniesHouseDocumentService!!,
				mockPreferencesHelper!!,
				base64Wrapper!!,
				constantsHelper!!,
				filingHistoryDescriptionsHelper!!,
				firebaseAnalytics!!
		)*/
		//authorization = "Basic WnBoWHBnLXRyZndBTmlUTmZlNHh3SzZRWFk0WHdSd3cwd0h4RjVkbQ==";
		//companySearchResult = CompanySearchResult()
		every {
			companiesRepository.searchCompanies(any(), any())
		} answers
				{
					Single.create { companySearchResult }
				}
		RxJavaPlugins.setInitIoSchedulerHandler { immediate }
		RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
		RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
		RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
		RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
	}

	@Test
	fun test_searchCompanies_successful() {
		val testSubscriber = TestObserver<CompanySearchResult>()
		companiesRepository.searchCompanies("Games", "0").subscribe(testSubscriber)
		//testSubscriber.assertTerminated()
		//testSubscriber.assertValue(companySearchResult)
		//testSubscriber.assertComplete()
		testSubscriber.assertNoErrors()
	}
}
