package com.babestudios.companyinfouk.ui.favourites;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

class FavouritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private FavouritesRecyclerViewClickListener mItemListener;

	private ArrayList<SearchHistoryItem> searchHistoryItems = new ArrayList<>();

	private ArrayList<SearchHistoryItem> searchHistoryItemsPendingRemoval = new ArrayList<>();
	private static final int PENDING_REMOVAL_TIMEOUT = 5000; // 5sec
	private Handler handler = new Handler();
	private HashMap<SearchHistoryItem, Runnable> pendingRunnables = new HashMap<>();

	FavouritesAdapter(Context c, ArrayList<SearchHistoryItem> searchHistoryItems) {
		mItemListener = (FavouritesRecyclerViewClickListener) c;
		this.searchHistoryItems = searchHistoryItems;
	}

	void updateAdapter(ArrayList<SearchHistoryItem> searchHistoryItems) {
		this.searchHistoryItems = searchHistoryItems;
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.favourites_list_item, parent, false);

		return new FavouritesViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		FavouritesViewHolder favouritesViewHolder = (FavouritesViewHolder) viewHolder;
		final SearchHistoryItem item = searchHistoryItems.get(position);

		if (searchHistoryItemsPendingRemoval.contains(item)) {
			// we need to show the "undo" state of the row
			favouritesViewHolder.itemView.setBackgroundColor(Color.RED);
			favouritesViewHolder.ll.setVisibility(View.INVISIBLE);
			favouritesViewHolder.undoButton.setVisibility(View.VISIBLE);
			favouritesViewHolder.undoButton.setOnClickListener(v -> {
				// user wants to undo the removal, let's cancel the pending task
				Runnable pendingRemovalRunnable = pendingRunnables.get(item);
				pendingRunnables.remove(item);
				if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
				searchHistoryItemsPendingRemoval.remove(item);
				// this will rebind the row in "normal" state
				notifyItemChanged(searchHistoryItems.indexOf(item));
			});
		} else {
			favouritesViewHolder.lblCompanyName.setText(searchHistoryItems.get(position).companyName);
			favouritesViewHolder.lblCompanyNumber.setText(searchHistoryItems.get(position).companyNumber);
			favouritesViewHolder.undoButton.setVisibility(View.GONE);
			favouritesViewHolder.undoButton.setOnClickListener(null);
		}
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return searchHistoryItems.size();
	}

	class TitleViewHolder extends RecyclerView.ViewHolder {
		@Bind(R.id.lblTitle)
		TextView lblTitle;

		public TitleViewHolder(View itemView) {
			super(itemView);
		}
	}


	class FavouritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Bind(R.id.lblCompanyName)
		TextView lblCompanyName;
		@Bind(R.id.lblCompanyNumber)
		TextView lblCompanyNumber;
		@Bind(R.id.undo_button)
		Button undoButton;
		@Bind(R.id.ll)
		LinearLayout ll;

		FavouritesViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mItemListener.favouritesResultItemClicked(v, this.getLayoutPosition(), searchHistoryItems.get(getLayoutPosition()).companyName, searchHistoryItems.get(getLayoutPosition()).companyNumber);
		}
	}

	interface FavouritesRecyclerViewClickListener {
		void favouritesResultItemClicked(View v, int position, String companyName, String companyNumber);
		void removeFavourite(SearchHistoryItem favouriteToRemove);
	}

	void pendingRemoval(int position) {
		final SearchHistoryItem item = searchHistoryItems.get(position);
		if (!searchHistoryItemsPendingRemoval.contains(item)) {
			searchHistoryItemsPendingRemoval.add(item);
			// this will redraw row in "undo" state
			notifyItemChanged(position);
			// let's create, store and post a runnable to remove the item
			Runnable pendingRemovalRunnable = () -> {
				remove(searchHistoryItems.indexOf(item));
				mItemListener.removeFavourite(item);
			};
			handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
			pendingRunnables.put(item, pendingRemovalRunnable);
		}
	}

	private void remove(int position) {
		SearchHistoryItem item = searchHistoryItems.get(position);
		if (searchHistoryItemsPendingRemoval.contains(item)) {
			searchHistoryItemsPendingRemoval.remove(item);
		}
		if (searchHistoryItems.contains(item)) {
			searchHistoryItems.remove(position);
			notifyItemRemoved(position);
		}
	}

	boolean isPendingRemoval(int position) {
		SearchHistoryItem item = searchHistoryItems.get(position);
		return searchHistoryItemsPendingRemoval.contains(item);
	}
}
