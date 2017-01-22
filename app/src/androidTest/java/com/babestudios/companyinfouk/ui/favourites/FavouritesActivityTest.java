package com.babestudios.companyinfouk.ui.favourites;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.TestApplicationComponent;
import com.babestudios.companyinfouk.TestCompaniesHouseApplication;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;

import net.grandcentrix.thirtyinch.TiConfiguration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FavouritesActivityTest {

	@Inject
	FavouritesPresenter favouritesPresenter;

	@Rule
	public ActivityTestRule<FavouritesActivity> mActivityTestRule = new ActivityTestRule<>(FavouritesActivity.class, true, true);

	//TODO Test when coming from RecentSearchesActivity, shows data
	//TODO Test when coming from RecentSearchesActivity, not crashing on empty data
	//TODO Test when coming back from CompanyActivity after delete -> Updated

	@Before
	public void setUp() {
		Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
		TestCompaniesHouseApplication app = (TestCompaniesHouseApplication) instrumentation.getTargetContext().getApplicationContext();
		TestApplicationComponent component = app.getApplicationComponent();
		component.inject(this);

		//TODO How to inject this into the activity under test?

		SearchHistoryItem[] searchHistoryItems = new SearchHistoryItem[1];
		SearchHistoryItem searchHistoryItem = new SearchHistoryItem("Acme Painting", "12345678", 12);
		searchHistoryItems[0] = searchHistoryItem;
		when(favouritesPresenter.dataManager.getFavourites()).thenReturn(searchHistoryItems);
		when(favouritesPresenter.getConfig()).thenReturn(new TiConfiguration.Builder().setRetainPresenterEnabled(false).build());
	}

	@Test
	public void favouritesActivityTest() {
		ViewInteraction companyName = onView(withId(R.id.lblCompanyName));
		companyName.check(matches(withText("Acme Painting")));
	}

}
