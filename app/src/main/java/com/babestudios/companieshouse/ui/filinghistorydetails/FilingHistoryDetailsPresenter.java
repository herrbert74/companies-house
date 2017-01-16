package com.babestudios.companieshouse.ui.filinghistorydetails;

import android.net.Uri;
import android.os.Environment;

import com.babestudios.companieshouse.data.DataManager;

import net.grandcentrix.thirtyinch.TiPresenter;

import java.io.File;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observer;

public class FilingHistoryDetailsPresenter extends TiPresenter<FilingHistoryDetailsActivityView> implements Observer<ResponseBody> {

	DataManager dataManager;

	@Inject
	public FilingHistoryDetailsPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onWakeUp() {
		super.onWakeUp();
		getView().showProgress();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	public void getDocument() {
		dataManager.getDocument(getView().getFilingHistoryItemString()).subscribe(this);
	}

	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onError(Throwable e) {

	}

	@Override
	public void onNext(ResponseBody responseBody) {
		getView().checkPermissionAndWritePdf(responseBody);
	}

	void writePdf(ResponseBody responseBody) {
		File pdfFile = dataManager.writeDocumentPdf(responseBody);
		getView().showDocument(Uri.fromFile(pdfFile));
	}


}
