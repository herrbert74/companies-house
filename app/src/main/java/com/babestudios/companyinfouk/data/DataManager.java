package com.babestudios.companyinfouk.data;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.babestudios.companyinfouk.BuildConfig;
import com.babestudios.companyinfouk.data.local.ApiLookupHelper;
import com.babestudios.companyinfouk.data.local.PreferencesHelper;
import com.babestudios.companyinfouk.data.model.charges.Charges;
import com.babestudios.companyinfouk.data.model.company.Company;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList;
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency;
import com.babestudios.companyinfouk.data.model.officers.Officers;
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments;
import com.babestudios.companyinfouk.data.model.persons.Persons;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService;
import com.babestudios.companyinfouk.data.network.CompaniesHouseService;
import com.babestudios.companyinfouk.utils.Base64Wrapper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class DataManager {

	private final String authorization;

	private CompaniesHouseService companiesHouseService;
	private CompaniesHouseDocumentService companiesHouseDocumentService;
	private PreferencesHelper preferencesHelper;
	private ApiLookupHelper apiLookupHelper = new ApiLookupHelper();

	@Inject
	public DataManager(CompaniesHouseService companiesHouseService, CompaniesHouseDocumentService companiesHouseDocumentService, PreferencesHelper preferencesHelper, Base64Wrapper base64Wrapper) {
		this.companiesHouseService = companiesHouseService;
		this.companiesHouseDocumentService = companiesHouseDocumentService;
		this.preferencesHelper = preferencesHelper;
		authorization = "Basic " + base64Wrapper.encodeToString(BuildConfig.COMPANIES_HOUSE_API_KEY.getBytes(), Base64.NO_WRAP);
	}

	public String sicLookup(String code) {
		return apiLookupHelper.sicLookup(code);
	}

	public String accountTypeLookup(String accountType) {
		return apiLookupHelper.accountTypeLookup(accountType);
	}

	public String filingHistoryLookup(String filingHistory) {
		return apiLookupHelper.filingHistoryDescriptionLookup(filingHistory);
	}

	public Observable<CompanySearchResult> searchCompanies(CharSequence queryText, String startItem) {
		return companiesHouseService.searchCompanies(authorization, queryText.toString(), BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	public ArrayList<SearchHistoryItem> addRecentSearchItem(SearchHistoryItem searchHistoryItem) {
		return preferencesHelper.addRecentSearch(searchHistoryItem);
	}

	public SearchHistoryItem[] getRecentSearches() {
		return preferencesHelper.getRecentSearches();
	}

	public Observable<Company> getCompany(String companyNumber) {
		return companiesHouseService.getCompany(authorization, companyNumber)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread());
	}

	public Observable<FilingHistoryList> getFilingHistory(String companyNumber, String category, String startItem) {
		return companiesHouseService.getFilingHistory(authorization, companyNumber, category, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread());
	}

	public Observable<Charges> getCharges(String companyNumber, String startItem) {
		return companiesHouseService.getCharges(authorization, companyNumber, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread());
	}

	public Observable<Insolvency> getInsolvency(String companyNumber) {
		return companiesHouseService.getInsolvency(authorization, companyNumber)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread());
	}

	public Observable<Officers> getOfficers(String companyNumber, String startItem) {
		return companiesHouseService.getOfficers(authorization, companyNumber, null, null, null, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread());
	}

	public Observable<Appointments> getOfficerAppointments(String officerId, String startItem) {
		return companiesHouseService.getOfficerAppointments(authorization, officerId, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread());
	}

	public Observable<Persons> getPersons(String companyNumber, String startItem) {
		return companiesHouseService.getPersons(authorization, companyNumber, null, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread());
	}

	public Observable<ResponseBody> getDocument(String filingHistoryItemString) {
		Gson gson = new Gson();
		FilingHistoryItem filingHistoryItem = gson.fromJson(filingHistoryItemString, FilingHistoryItem.class);
		String documentId = filingHistoryItem.links.documentMetadata.replace("https://document-api.companieshouse.gov.uk/document/", "");
		return companiesHouseDocumentService.getDocument(authorization, "application/pdf", documentId)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread());
	}

	@NonNull
	public File writeDocumentPdf(ResponseBody responseBody) {
		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/download");
		dir.mkdirs();
		File pdfFile = new File(dir, "doc.pdf");
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			byte[] fileReader = new byte[4096];
			try {
				inputStream = responseBody.byteStream();
				outputStream = new FileOutputStream(pdfFile);
				while (true) {
					int read = inputStream.read(fileReader);
					if (read == -1) {
						break;
					}
					outputStream.write(fileReader, 0, read);
				}
				outputStream.flush();
			} catch (IOException e) {
				Log.d("test", e.getLocalizedMessage());
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}

				if (outputStream != null) {
					outputStream.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("test", "Error during closing input stream" + e.getLocalizedMessage());
		}
		return pdfFile;
	}

	public void clearAllRecentSearches() {
		preferencesHelper.clearAllRecentSearches();
	}

	public boolean addFavourite(SearchHistoryItem searchHistoryItem) {
		return preferencesHelper.addFavourite(searchHistoryItem);
	}

	public boolean isFavourite(SearchHistoryItem searchHistoryItem) {
		SearchHistoryItem[] items = preferencesHelper.getFavourites();
		if(items != null) {
			ArrayList<SearchHistoryItem> favourites = new ArrayList<>(Arrays.asList(items));
			return favourites.contains(searchHistoryItem);
		} else {
			return false;
		}
	}

	public SearchHistoryItem[] getFavourites() {
		return preferencesHelper.getFavourites();
	}

	public void removeFavourite(SearchHistoryItem favouriteToRemove) {
		preferencesHelper.removeFavourite(favouriteToRemove);
	}
}
