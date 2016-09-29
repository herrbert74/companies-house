package com.babestudios.companieshouse.data.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class SearchItem implements Comparable<SearchItem>,Parcelable {

	public CharSequence queryText;
	public long searchTime;


	public SearchItem(CharSequence queryText, long searchTime) {
		this.queryText = queryText;
		this.searchTime = searchTime;
	}

	@Override
	public int compareTo(@NonNull SearchItem searchItem) {
		return this.searchTime < searchItem.searchTime ? -1 : this.searchTime > searchItem.searchTime ? 1 : 0;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		TextUtils.writeToParcel(this.queryText, dest, 0);
		dest.writeLong(this.searchTime);
	}

	protected SearchItem(Parcel in) {
		this.queryText = in.readParcelable(CharSequence.class.getClassLoader());
		this.searchTime = in.readLong();
	}

	public static final Parcelable.Creator<SearchItem> CREATOR = new Parcelable.Creator<SearchItem>() {
		@Override
		public SearchItem createFromParcel(Parcel source) {
			return new SearchItem(source);
		}

		@Override
		public SearchItem[] newArray(int size) {
			return new SearchItem[size];
		}
	};
}
