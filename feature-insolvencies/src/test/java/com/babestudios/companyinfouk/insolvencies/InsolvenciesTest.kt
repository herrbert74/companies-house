package com.babestudios.companyinfouk.insolvencies

import com.airbnb.mvrx.test.MvRxTestRule
import com.babestudios.base.ext.callPrivateFunc
import com.babestudios.base.ext.getPrivateProperty
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesState
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesViewModel
import com.babestudios.companyinfouk.navigation.features.InsolvenciesBaseNavigatable
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class InsolvenciesTest {

	private val companiesHouseRepository = mockk<CompaniesRxRepository>()

	private val insolvenciesNavigator = mockk<InsolvenciesBaseNavigatable>()

	@Before
	fun setUp() {
		every {
			companiesHouseRepository.getInsolvency("123")
		} answers
				{
					Single.create { Insolvency() }
				}
	}

	@Test
	fun whenGetInsolvencies_thenRepoGetInsolvenciesIsCalled() {
		val viewModel = insolvenciesViewModel()
		val func = viewModel.callPrivateFunc("fetchInsolvencies", "123")
		val repo :CompaniesRxRepository? = viewModel.getPrivateProperty("companiesRepository")
		//Executed twice because it's also in init function
		verify(exactly = 2) { repo?.getInsolvency("123") }
	}

	private fun insolvenciesViewModel(): InsolvenciesViewModel {
		return InsolvenciesViewModel(
				InsolvenciesState(companyNumber = "123", totalInsolvenciesCount = 100),
				companiesHouseRepository,
				insolvenciesNavigator,
				"dates",
				"practitioners")
	}

	companion object {
		@JvmField
		@ClassRule
		val mvrxTestRule = MvRxTestRule()
	}
}
