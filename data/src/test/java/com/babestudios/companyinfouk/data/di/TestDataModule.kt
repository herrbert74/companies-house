package com.babestudios.companyinfouk.data.di

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.common.model.charges.Charges
import com.babestudios.companyinfouk.common.model.company.Company
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.common.model.insolvency.Insolvency
import com.babestudios.companyinfouk.common.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.common.model.officers.OfficersResponse
import com.babestudios.companyinfouk.common.model.persons.Person
import com.babestudios.companyinfouk.common.model.persons.PersonsResponse
import com.babestudios.companyinfouk.core.mappers.mapNullInputList
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.local.PREF_FILE_NAME
import com.babestudios.companyinfouk.data.local.apilookup.*
import com.babestudios.companyinfouk.data.mappers.*
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.data.model.persons.PersonDto
import com.babestudios.companyinfouk.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.data.utils.StringResourceHelper
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@Suppress("unused")
class TestDataModule(private val context: Context) {

	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	internal fun provideCompaniesHouseRetrofit(): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		httpClient.addInterceptor(ChuckerInterceptor(context))
		return Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.client(httpClient.build())//
				.build()
	}

	@Provides
	@Singleton
	@Named("CompaniesHouseDocumentRetrofit")
	internal fun provideCompaniesHouseDocumentRetrofit(): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		return Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_DOCUMENT_API_BASE_URL)//
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.client(httpClient.build())//
				.build()
	}

	@Provides
	@Singleton
	internal fun provideCompaniesHouseService(@Named("CompaniesHouseRetrofit") retroFit: Retrofit)
			: CompaniesHouseService {
		return retroFit.create(CompaniesHouseService::class.java)
	}

	@Provides
	@Singleton
	internal fun provideCompaniesHouseDocumentService(@Named("CompaniesHouseDocumentRetrofit") retroFit: Retrofit)
			: CompaniesHouseDocumentService {
		return retroFit.create(CompaniesHouseDocumentService::class.java)
	}

	@Provides
	internal fun provideGson(): Gson {
		return GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
				.create()
	}

	@Provides
	internal fun provideSharedPreferences(): SharedPreferences {
		return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
	}

	@Provides
	@Singleton
	internal fun provideSchedulerProvider(): SchedulerProvider {
		return SchedulerProvider(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR), AndroidSchedulers.mainThread())
	}

	@Provides
	@Singleton
	internal fun provideFirebaseAnalytics(): FirebaseAnalytics {
		return FirebaseAnalytics.getInstance(context)
	}

	//region Mappers

	@Provides
	fun provideMapFilingHistoryDto(filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelperContract)
			: (FilingHistoryDto) -> FilingHistory =
			{ filingHistoryDto ->
				mapFilingHistoryDto(filingHistoryDto) { items ->
					mapNullInputList(items) {
						mapFilingHistoryItemDto(
								it,
								filingHistoryDescriptionsHelper,
								{ linksDto ->
									mapFilingHistoryLinks(linksDto)
								},
								{ categoryDto ->
									mapFilingHistoryCategoryDto(categoryDto)
								})
					}
				}
			}

	@Provides
	fun provideMapChargesDto(chargesHelper: ChargesHelperContract)
			: (ChargesDto) -> Charges =
			{ chargesDto ->
				mapChargesDto(chargesDto) { items ->
					mapNullInputList(items) {
						mapChargesItemDto(
								it,
								chargesHelper,
								{ transactionDtoList ->
									mapNullInputList(transactionDtoList) { transactionsDto ->
										transactionsDto.deliveredOn.orEmpty()
										mapTransactionDto(
												transactionsDto,
												chargesHelper
										)
									}
								},
								{ particularsDto ->
									mapParticularsDto(particularsDto)
								})
					}
				}
			}

	@Provides
	fun provideMapCompanyDto(
			constantsHelper: ConstantsHelperContract,
			stringResourceHelperContract: StringResourceHelperContract,
	): (CompanyDto) -> Company =
			{ companyDto ->
				mapCompanyDto(
						companyDto,
						{ accounts -> mapAccountsDto(accounts, constantsHelper, stringResourceHelperContract) },
						{ registeredOfficeAddress -> mapAddressDto(registeredOfficeAddress) },
						{ sicCodes -> mapNatureOfBusiness(sicCodes, constantsHelper) },
				)
			}

	@Provides
	fun provideMapInsolvencyDto(constantsHelper: ConstantsHelperContract): (InsolvencyDto) -> Insolvency =
			{ insolvencyDto ->
				mapInsolvencyDto(insolvencyDto)
				{ cases ->
					mapNullInputList(cases)
					{ case ->
						mapInsolvencyCaseDto(
								case,
								{ dates ->
									mapNullInputList(dates) { date ->
										mapDateDto(date, constantsHelper)
									}
								},
								{ practitioners ->
									mapNullInputList(practitioners) { practitioner ->
										mapPractitionerDto(practitioner) { addressDto ->
											mapAddressDto(addressDto)
										}
									}
								},
								constantsHelper,
						)
					}
				}
			}

	@Provides
	fun provideMapOfficerResponseDto(
			constantsHelper: ConstantsHelperContract,
			stringResourceHelper: StringResourceHelperContract
	): (OfficersResponseDto) ->
	OfficersResponse =
			{ officersResponseDto ->
				mapOfficersResponseDto(officersResponseDto)
				{ officers ->
					mapNullInputList(officers)
					{ officer ->
						mapOfficerDto(
								officer,
								{ linksDto -> mapOfficerLinksDto(linksDto) },
								{ registeredOfficeAddressDto -> mapAddressDto(registeredOfficeAddressDto) },
								{ monthYearDto -> mapMonthYearDto(monthYearDto) },
								constantsHelper,
								stringResourceHelper
						)
					}
				}
			}

	@Provides
	fun provideMapAppointmentsResponseDto(constantsHelper: ConstantsHelperContract): (AppointmentsResponseDto) ->
	AppointmentsResponse =
			{ appointmentsResponseDto ->
				mapAppointmentsResponseDto(
						appointmentsResponseDto,
						{ appointments ->
							mapNullInputList(appointments)
							{ appointmentDto ->
								mapAppointmentDto(
										appointmentDto,
										{ appointedToDto -> mapAppointedToDto(appointedToDto) },
										{ addressDto -> mapAddressDto(addressDto) },
										constantsHelper,
								)
							}
						},
						{ monthYearDto -> mapMonthYearDto(monthYearDto) },
				)
			}

	@Provides
	fun provideMapPersonDto(pscHelper: PscHelperContract): (PersonDto) -> Person =
			{ personDto ->
				mapPersonDto(
						personDto,
						pscHelper,
						{ addressDto -> mapAddressDto(addressDto) },
						{ monthYearDto -> mapMonthYearDto(monthYearDto) },
				)
			}


	//TODO Why I cannot use the result above?
	@Provides
	fun provideMapPersonsResponseDto(pscHelper: PscHelperContract): (PersonsResponseDto) -> PersonsResponse =
			{ personsResponseDto ->
				mapPersonsResponseDto(personsResponseDto) { items ->
					mapNullInputList(items) { personDto ->
						mapPersonDto(
								personDto,
								pscHelper,
								{ addressDto -> mapAddressDto(addressDto) },
								{ monthYearDto -> mapMonthYearDto(monthYearDto) },
						)
					}
				}
			}

	@Provides
	@Singleton
	fun provideStringResourceHelper(): StringResourceHelperContract {
		val stringResourceHelper = mockk<StringResourceHelper>()
		val slotDate = slot<String>()
		val slotFrom = slot<String>()
		val slotFrom2 = slot<String>()
		val slotTo = slot<String>()
		every { stringResourceHelper.getCompanyAccountsNotFoundString() } returns ""
		every { stringResourceHelper.getLastAccountMadeUpToString(any(), capture(slotDate)) } answers {
			"Last account made up to ${slotDate.captured}"
		}
		every { stringResourceHelper.getAppointedFromString(capture(slotFrom)) } answers {
			String.format("From %1\$s", slotFrom.captured)
		}
		every { stringResourceHelper.getAppointedFromToString(capture(slotFrom2), capture(slotTo)) } answers {
			String.format("From %1\$s to %2\$s", slotFrom2.captured, slotTo.captured)
		}
		return stringResourceHelper
	}

	@Provides
	@Singleton
	fun provideConstantsHelper(): ConstantsHelperContract {
		val constantsHelper = mockk<ConstantsHelper>()
		every { constantsHelper.sicLookUp("68100") } returns "Buying and selling of own real estate"
		every { constantsHelper.accountTypeLookUp(any()) } returns ""
		every { constantsHelper.insolvencyCaseType(any()) } returns ""
		every { constantsHelper.insolvencyCaseDateType(any()) } returns ""
		every { constantsHelper.officerRoleLookup(any()) } returns ""
		return constantsHelper
	}

	@Provides
	@Singleton
	fun provideFilingHistoryDescriptionsHelper(): FilingHistoryDescriptionsHelperContract {
		val filingHistoryDescriptionsHelper = mockk<FilingHistoryDescriptionsHelper>()
		every { filingHistoryDescriptionsHelper.filingHistoryLookUp(any()) } returns ""
		return filingHistoryDescriptionsHelper
	}

	@Provides
	@Singleton
	fun provideChargesHelper(): ChargesHelperContract {
		val chargesHelper = mockk<ChargesHelper>()
		every { chargesHelper.statusLookUp(any()) } returns ""
		every { chargesHelper.filingTypeLookUp(any()) } returns ""
		return chargesHelper
	}

	@Provides
	@Singleton
	fun providePscHelper(): PscHelperContract {
		val pscHelper = mockk<PscHelperContract>()
		every { pscHelper.shortDescriptionLookUp(any()) } returns ""
		every { pscHelper.kindLookUp(any()) } returns ""
		return pscHelper
	}

	//endregion

}
