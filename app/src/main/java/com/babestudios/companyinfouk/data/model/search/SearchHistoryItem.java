package com.babestudios.companyinfouk.data.model.search;


import java.util.Objects;

public class SearchHistoryItem {

	public String companyName;
	public String companyNumber;
	private long searchTime;


	public SearchHistoryItem(String companyName, String companyNumber, long searchTime) {
		this.companyName = companyName;
		this.companyNumber = companyNumber;
		this.searchTime = searchTime;
	}

	@Override
	public boolean equals(Object searchItem){
		if (searchItem == null) {
			return false;
		}
		if (!SearchHistoryItem.class.isAssignableFrom(searchItem.getClass())) {
			return false;
		}
		final SearchHistoryItem other = (SearchHistoryItem) searchItem;
		return Objects.equals(this.companyNumber, other.companyNumber);
	}

}
