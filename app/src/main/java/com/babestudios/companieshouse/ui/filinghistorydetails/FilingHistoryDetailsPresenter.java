package com.babestudios.companieshouse.ui.filinghistorydetails;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.filinghistory.FilingHistoryItem;
import com.google.gson.Gson;

import net.grandcentrix.thirtyinch.TiPresenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observer;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FilingHistoryDetailsPresenter extends TiPresenter<FilingHistoryDetailsActivityView> implements Observer<ResponseBody> {

	private DataManager dataManager;
	private Context context;


	@Inject
	public FilingHistoryDetailsPresenter(DataManager dataManager, Context context) {
		this.dataManager = dataManager;
		this.context = context;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		//CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);

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
		Gson gson = new Gson();
		FilingHistoryItem filingHistoryItem = gson.fromJson(getView().getFilingHistoryItemString(), FilingHistoryItem.class);
		String documentId = filingHistoryItem.links.documentMetadata.replace("https://document-api.companieshouse.gov.uk/document/", "");
		dataManager.getDocument(documentId).subscribe(this);
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

	public void writePdf(ResponseBody responseBody) {
		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/download");
		dir.mkdirs();
		File pdfFile = new File(dir, "doc.pdf");
		//File pdfFile = new File(context.getFilesDir().getPath() + "/doc.pdf");
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
			Log.d("test", "Error during closing inputstream");
		}
		getView().showDocument(Uri.fromFile(pdfFile));
	}
}
