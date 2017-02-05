package com.babestudios.companyinfouk.ui.chargesdetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.charges.ChargesItem;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.babestudios.companyinfouk.utils.DividerItemDecorationWithSubHeading;
import com.google.gson.Gson;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargesDetailsActivity extends CompositeActivity{

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.charges_details_recycler_view)
	RecyclerView chargesDetailsRecyclerView;

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public ChargesDetailsActivity() {
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charges_details);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

		ButterKnife.bind(this);
		String chargesItemString = getIntent().getStringExtra("chargesItem");
		Gson gson = new Gson();
		ChargesItem chargesItem = gson.fromJson(chargesItemString, ChargesItem.class);

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.charges_details);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		createChargesDetailsRecyclerView(chargesItem);
	}

	private void createChargesDetailsRecyclerView(ChargesItem chargesItem) {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		chargesDetailsRecyclerView.setLayoutManager(linearLayoutManager);
		ArrayList<Integer> titlePositions = new ArrayList<>();
		titlePositions.add(0);
		chargesDetailsRecyclerView.addItemDecoration(
				new DividerItemDecorationWithSubHeading(this, titlePositions));
		ChargesDetailsAdapter adapter = new ChargesDetailsAdapter(this, chargesItem);
		chargesDetailsRecyclerView.setAdapter(adapter);
	}
}
