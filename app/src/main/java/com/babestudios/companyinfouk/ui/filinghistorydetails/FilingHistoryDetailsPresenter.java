package com.babestudios.companyinfouk.ui.filinghistorydetails;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.babestudios.companyinfouk.data.DataManager;

import net.grandcentrix.thirtyinch.TiPresenter;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observer;

public class FilingHistoryDetailsPresenter extends TiPresenter<FilingHistoryDetailsActivityView> implements Observer<ResponseBody> {

	DataManager dataManager;

	ResponseBody responseBody;

	@Inject
	public FilingHistoryDetailsPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onAttachView(@NonNull FilingHistoryDetailsActivityView view) {
		super.onAttachView(view);
		if(responseBody != null) {
			view.checkPermissionAndWritePdf(responseBody);
		} else {
			view.showProgress();
		}
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
		Log.d("test", "onError: " + e.fillInStackTrace());
		if(getView()!= null) {
			getView().showError();
		}
	}

	@Override
	public void onNext(ResponseBody responseBody) {
		this.responseBody = responseBody;
		getView().checkPermissionAndWritePdf(responseBody);
	}

	void writePdf(ResponseBody responseBody) {
		this.responseBody = null;
		getView().showDocument(dataManager.writeDocumentPdf(responseBody));
	}


}
