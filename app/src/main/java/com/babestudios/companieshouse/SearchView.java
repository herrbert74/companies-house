package com.babestudios.companieshouse;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

public interface SearchView extends TiView {

	@CallOnMainThread
	void showText(final String text);
}