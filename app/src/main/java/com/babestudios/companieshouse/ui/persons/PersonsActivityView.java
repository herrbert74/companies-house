package com.babestudios.companieshouse.ui.persons;

import com.babestudios.companieshouse.data.model.persons.Persons;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

interface PersonsActivityView extends TiView {

	@CallOnMainThread
	void showProgress();

	@CallOnMainThread
	void hideProgress();

	@CallOnMainThread
	void showPersons(final Persons persons);

	@CallOnMainThread
	void showNoPersons();

	@CallOnMainThread
	String getCompanyNumber();

}