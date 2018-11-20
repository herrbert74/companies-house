package com.babestudios.companyinfouk.ui.favourites;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.TestCompaniesHouseApplication;
import com.babestudios.companyinfouk.injection.AndroidTestApplicationComponent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.babestudios.companyinfouk.TestUtils.withRecyclerView;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FavouritesActivityTest {

	@Rule
	public ActivityTestRule<FavouritesActivity> mActivityTestRule = new ActivityTestRule<>(FavouritesActivity.class);

	//TODO Test when coming from RecentSearchesActivity, shows data
	//TODO Test when coming from RecentSearchesActivity, not crashing on empty data
	//TODO Test when coming back from CompanyActivity after delete -> Updated

	@Before
	public void setUp() {
		Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
		TestCompaniesHouseApplication app = (TestCompaniesHouseApplication) instrumentation.getTargetContext().getApplicationContext();
		AndroidTestApplicationComponent component = app.getApplicationComponent();
		component.inject(mActivityTestRule);
	}

	@Test
	public void whenComingFromRecentSearchesActivity_showsData() {
		onView(withRecyclerView(R.id.rvFavourites).atPositionOnView(0, R.id.lblCompanyName)).check(matches(withText("Acme Painting")));
	}

}
