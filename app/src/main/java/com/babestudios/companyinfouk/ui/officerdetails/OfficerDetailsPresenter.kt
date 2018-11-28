package com.babestudios.companyinfouk.ui.officerdetails

import net.grandcentrix.thirtyinch.TiPresenter

import javax.inject.Inject

class OfficerDetailsPresenter @Inject
constructor() : TiPresenter<OfficerDetailsActivityView>() {

	override fun onCreate() {
		super.onCreate()
	}

	override fun onAttachView(view: OfficerDetailsActivityView) {
		super.onAttachView(view)
		view.showProgress()
	}

	override fun onDestroy() {
		super.onDestroy()
	}


}
