package com.babestudios.companyinfouk.data.di

import android.content.Context
import android.content.SharedPreferences
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.domain.model.charges.Charges
import com.babestudios.companyinfouk.domain.model.company.Company
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.core.mappers.mapNullInputList
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.local.PREF_FILE_NAME
import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.PscHelper
import com.babestudios.companyinfouk.data.local.apilookup.PscHelperContract
import com.babestudios.companyinfouk.data.mappers.mapAccountsDto
import com.babestudios.companyinfouk.data.mappers.mapAddressDto
import com.babestudios.companyinfouk.data.mappers.mapAppointedToDto
import com.babestudios.companyinfouk.data.mappers.mapAppointmentDto
import com.babestudios.companyinfouk.data.mappers.mapAppointmentsResponseDto
import com.babestudios.companyinfouk.data.mappers.mapChargesDto
import com.babestudios.companyinfouk.data.mappers.mapChargesItemDto
import com.babestudios.companyinfouk.data.mappers.mapCompanyDto
import com.babestudios.companyinfouk.data.mappers.mapDateDto
import com.babestudios.companyinfouk.data.mappers.mapFilingHistoryCategoryDto
import com.babestudios.companyinfouk.data.mappers.mapFilingHistoryDto
import com.babestudios.companyinfouk.data.mappers.mapFilingHistoryItemDto
import com.babestudios.companyinfouk.data.mappers.mapFilingHistoryLinks
import com.babestudios.companyinfouk.data.mappers.mapInsolvencyCaseDto
import com.babestudios.companyinfouk.data.mappers.mapInsolvencyDto
import com.babestudios.companyinfouk.data.mappers.mapMonthYearDto
import com.babestudios.companyinfouk.data.mappers.mapNatureOfBusiness
import com.babestudios.companyinfouk.data.mappers.mapOfficerDto
import com.babestudios.companyinfouk.data.mappers.mapOfficerLinksDto
import com.babestudios.companyinfouk.data.mappers.mapOfficersResponseDto
import com.babestudios.companyinfouk.data.mappers.mapParticularsDto
import com.babestudios.companyinfouk.data.mappers.mapPersonDto
import com.babestudios.companyinfouk.data.mappers.mapPersonsResponseDto
import com.babestudios.companyinfouk.data.mappers.mapPractitionerDto
import com.babestudios.companyinfouk.data.mappers.mapTransactionDto
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.data.model.persons.PersonDto
import com.babestudios.companyinfouk.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseRxDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseRxService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import com.babestudios.companyinfouk.data.network.interceptors.CompaniesHouseInterceptor
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.data.utils.StringResourceHelper
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@Suppress("unused")
class DataModule(private val context: Context) {

	@Provides
	@Singleton
	@Named("CompaniesHouseRetrofit")
	internal fun provideCompaniesHouseRetrofit(companiesHouseInterceptor: CompaniesHouseInterceptor): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		httpClient.addInterceptor(
			ChuckerInterceptor.Builder(context)
				.collector(ChuckerCollector(context, showNotification = false))
				.build()
		)
		httpClient.addInterceptor(companiesHouseInterceptor)
		return Retrofit.Builder()//
			.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(AdvancedGsonConverterFactory.create())
			.client(httpClient.build())
			.build()
	}

	@Provides
	@Singleton
	@Named("CompaniesHouseDocumentRetrofit")
	internal fun provideCompaniesHouseDocumentRetrofit(companiesHouseInterceptor: CompaniesHouseInterceptor): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor(logging)
		httpClient.addInterceptor(companiesHouseInterceptor)
		return Retrofit.Builder()//
			.baseUrl(BuildConfig.COMPANIES_HOUSE_DOCUMENT_API_BASE_URL)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(AdvancedGsonConverterFactory.create())
			.client(httpClient.build())
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
	@Singleton
	internal fun provideCompaniesHouseRxService(@Named("CompaniesHouseRetrofit") retroFit: Retrofit)
		: CompaniesHouseRxService {
		return retroFit.create(CompaniesHouseRxService::class.java)
	}

	@Provides
	@Singleton
	internal fun provideCompaniesHouseDocumentRxService(@Named("CompaniesHouseDocumentRetrofit") retroFit: Retrofit)
		: CompaniesHouseRxDocumentService {
		return retroFit.create(CompaniesHouseRxDocumentService::class.java)
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
		return SchedulerProvider(
			Schedulers.from(Executors.newCachedThreadPool()), AndroidSchedulers
			.mainThread()
		)
	}

	@Provides
	@Singleton
	internal fun provideFirebaseAnalytics(): FirebaseAnalytics {
		return FirebaseAnalytics.getInstance(context)
	}

	//region Mappers

	@Provides
	fun provideMapFilingHistoryDto(filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelper)
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
	): (OfficersResponseDto) -> OfficersResponse =
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
	fun provideMapAppointmentsResponseDto(constantsHelper: ConstantsHelperContract)
		: (AppointmentsResponseDto) -> AppointmentsResponse =
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

//endregion

	@Provides
	@Singleton
	fun provideStringResourceHelper(@ApplicationContext context: Context): StringResourceHelperContract =
		StringResourceHelper(context)

	@Provides
	@Singleton
	fun provideConstantsHelper(rawResourceHelper: RawResourceHelper): ConstantsHelperContract =
		ConstantsHelper(rawResourceHelper)

	@Provides
	@Singleton
	fun provideFilingHistoryDescriptionsHelper(rawResourceHelper: RawResourceHelper)
		: FilingHistoryDescriptionsHelperContract = FilingHistoryDescriptionsHelper(rawResourceHelper)

	@Provides
	@Singleton
	fun provideChargesHelper(rawResourceHelper: RawResourceHelper)
		: ChargesHelperContract = ChargesHelper(rawResourceHelper)

	@Provides
	@Singleton
	fun providePscHelper(rawResourceHelper: RawResourceHelper)
		: PscHelperContract = PscHelper(rawResourceHelper)

}
