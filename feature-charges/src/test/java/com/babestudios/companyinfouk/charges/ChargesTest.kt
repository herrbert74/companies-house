package com.babestudios.companyinfouk.charges

import com.airbnb.mvrx.test.MvRxTestRule
import com.babestudios.base.ext.getPrivateFieldWithReflection
import com.babestudios.companyinfouk.charges.ui.ChargesState
import com.babestudios.companyinfouk.charges.ui.ChargesViewModel
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.navigation.features.ChargesNavigator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class ChargesTest {

	private val companiesHouseRepository = mockk<CompaniesRepositoryContract>()

	private val chargesNavigator = mockk<ChargesNavigator>()

	@Before
	fun setUp() {
		every {
			companiesHouseRepository.fetchCharges("123", "0")
		} answers
				{
					Single.create { Charges() }
				}
	}

	@Test
	fun whenGetCharges_thenRepoGetChargesIsCalled() {
		val viewModel = chargesViewModel()
		viewModel.fetchCharges("123")
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) { repo.fetchCharges("123", "0") }
	}

	@Test
	fun whenLoadMoreCharges_thenRepoLoadMoreChargesIsCalled() {
		val viewModel = chargesViewModel()
		viewModel.loadMoreCharges(0)
		val repo = viewModel.getPrivateFieldWithReflection<CompaniesRepositoryContract>("companiesRepository")
		verify(exactly = 1) { repo.fetchCharges("123", "0") }
	}

	private fun chargesViewModel(): ChargesViewModel {
		return ChargesViewModel(
				ChargesState(companyNumber = "123", totalChargesCount = 100),
				companiesHouseRepository,
				chargesNavigator)
	}

	companion object {
		@JvmField
		@ClassRule
		val mvrxTestRule = MvRxTestRule()
	}
}
