package com.babestudios.companyinfouk.shared.domain.model.search

enum class FilterState(private val name2: String) {
	FILTER_SHOW_ALL("all"),
	FILTER_ACTIVE("active"),
	FILTER_LIQUIDATION("liquidation"),
	FILTER_OPEN("open"),
	FILTER_DISSOLVED("dissolved");

	override fun toString(): String {
		return name2
	}
}
