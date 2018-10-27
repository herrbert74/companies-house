package com.babestudios.companyinfouk.ui.privacy;

import android.os.Bundle;
import android.webkit.WebView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

public class PrivacyActivity extends CompositeActivity {



	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public PrivacyActivity() {
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView view = new WebView(this);
		view.getSettings().setJavaScriptEnabled(true);
		view.loadUrl("file:///android_asset/privacy_policy.html");
		setContentView(view);
	}

	@Override
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}
}
