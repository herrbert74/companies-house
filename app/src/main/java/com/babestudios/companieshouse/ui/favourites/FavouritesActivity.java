package com.babestudios.companieshouse.ui.favourites;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;
import com.babestudios.companieshouse.ui.company.CompanyActivity;
import com.babestudios.companieshouse.utils.DividerItemDecoration;

import net.grandcentrix.thirtyinch.TiActivity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FavouritesActivity extends TiActivity<FavouritesPresenter, FavouritesActivityView> implements FavouritesActivityView, FavouritesAdapter.FavouritesRecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.favourites_recycler_view)
	RecyclerView favouritesRecyclerView;

	private FavouritesAdapter favouritesAdapter;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourites);
		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		createRecentSearchesRecyclerView();
		setUpItemTouchHelper();
		setUpAnimationDecoratorHelper();
	}

	private void createRecentSearchesRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		favouritesRecyclerView.setLayoutManager(linearLayoutManager);
		favouritesRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));
	}


	@Override
	public void showProgress() {
		progressbar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progressbar.setVisibility(View.GONE);
	}

	@Override
	public void showFavourites(SearchHistoryItem[] searchHistoryItems) {
		favouritesRecyclerView.setVisibility(View.VISIBLE);
		if (favouritesRecyclerView.getAdapter() == null) {
			ArrayList<SearchHistoryItem> searchHistoryItemsList;
			if (searchHistoryItems != null) {
				searchHistoryItemsList = new ArrayList<>(Arrays.asList(searchHistoryItems));
			} else {
				searchHistoryItemsList = new ArrayList<>();
			}
			favouritesAdapter = new FavouritesAdapter(FavouritesActivity.this, searchHistoryItemsList);
			favouritesRecyclerView.setAdapter(favouritesAdapter);
		}
	}


	@NonNull
	@Override
	public FavouritesPresenter providePresenter() {
		return new FavouritesPresenter();
	}

	@Override
	public void favouritesResultItemClicked(View v, int position, String companyName, String companyNumber) {
		getPresenter().getCompany(companyNumber, companyName);
	}

	@Override
	public void removeFavourite(SearchHistoryItem favouriteToRemove) {
		getPresenter().removeFavourite(favouriteToRemove);
	}


	@Override
	public void startCompanyActivity(String companyNumber, String companyName) {
		Intent startIntent = new Intent(this, CompanyActivity.class);
		startIntent.putExtra("companyNumber", companyNumber);
		startIntent.putExtra("companyName", companyName);
		startActivity(startIntent);
	}

	private void setUpItemTouchHelper() {

		ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

			// we want to cache these and not allocate anything repeatedly in the onChildDraw method
			Drawable background;
			Drawable xMark;
			int xMarkMargin;
			boolean initiated;

			private void init() {
				background = new ColorDrawable(Color.RED);
				xMark = ContextCompat.getDrawable(FavouritesActivity.this, R.drawable.close_vector);
				//Set it to background color, because otherwise it will stay on top after half swipe
				TypedValue a = new TypedValue();
				getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
				int color = a.data;
				xMark.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
				xMarkMargin = (int) FavouritesActivity.this.getResources().getDimension(R.dimen.view_margin_small);
				initiated = true;
			}

			// not important, we don't want drag & drop
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
				int position = viewHolder.getAdapterPosition();
				if (favouritesAdapter.isPendingRemoval(position)) {
					return 0;
				}
				return super.getSwipeDirs(recyclerView, viewHolder);
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
				int swipedPosition = viewHolder.getAdapterPosition();
				favouritesAdapter.pendingRemoval(swipedPosition);
			}

			@Override
			public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				View itemView = viewHolder.itemView;

				// not sure why, but this method get's called for viewholder that are already swiped away
				if (viewHolder.getAdapterPosition() == -1) {
					// not interested in those
					return;
				}

				if (!initiated) {
					init();
				}

				// draw red background
				background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
				background.draw(c);

				// draw x mark
				int itemHeight = itemView.getBottom() - itemView.getTop();
				int intrinsicWidth = xMark.getIntrinsicWidth();
				int intrinsicHeight = xMark.getIntrinsicWidth();

				int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
				int xMarkRight = itemView.getRight() - xMarkMargin;
				int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
				int xMarkBottom = xMarkTop + intrinsicHeight;
				xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

				xMark.draw(c);

				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
			}

		};
		ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		mItemTouchHelper.attachToRecyclerView(favouritesRecyclerView);
	}

	private void setUpAnimationDecoratorHelper() {
		favouritesRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

			// we want to cache this and not allocate anything repeatedly in the onDraw method
			Drawable background;
			boolean initiated;

			private void init() {
				background = new ColorDrawable(Color.RED);
				initiated = true;
			}

			@Override
			public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

				if (!initiated) {
					init();
				}

				// only if animation is in progress
				if (parent.getItemAnimator().isRunning()) {

					// some items might be animating down and some items might be animating up to close the gap left by the removed item
					// this is not exclusive, both movement can be happening at the same time
					// to reproduce this leave just enough items so the first one and the last one would be just a little off screen
					// then remove one from the middle

					// find first child with translationY > 0
					// and last one with translationY < 0
					// we're after a rect that is not covered in recycler-view views at this point in time
					View lastViewComingDown = null;
					View firstViewComingUp = null;

					// this is fixed
					int left = 0;
					int right = parent.getWidth();

					// this we need to find out
					int top = 0;
					int bottom = 0;

					// find relevant translating views
					int childCount = parent.getLayoutManager().getChildCount();
					for (int i = 0; i < childCount; i++) {
						View child = parent.getLayoutManager().getChildAt(i);
						if (child.getTranslationY() < 0) {
							// view is coming down
							lastViewComingDown = child;
						} else if (child.getTranslationY() > 0) {
							// view is coming up
							if (firstViewComingUp == null) {
								firstViewComingUp = child;
							}
						}
					}

					if (lastViewComingDown != null && firstViewComingUp != null) {
						// views are coming down AND going up to fill the void
						top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
						bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
					} else if (lastViewComingDown != null) {
						// views are going down to fill the void
						top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
						bottom = lastViewComingDown.getBottom();
					} else if (firstViewComingUp != null) {
						// views are coming up to fill the void
						top = firstViewComingUp.getTop();
						bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
					}

					background.setBounds(left, top, right, bottom);
					background.draw(c);

				}
				super.onDraw(c, parent, state);
			}

		});
	}

}
