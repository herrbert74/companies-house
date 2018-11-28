package com.babestudios.companyinfouk.ui.officerdetails

import net.grandcentrix.thirtyinch.TiPresenter

import javax.inject.Inject

class OfficerDetailsPresenter @Inject
constructor() : TiPresenter<OfficerDetailsActivityView>() {

	override fun onAttachView(view: OfficerDetailsActivityView) {
		super.onAttachView(view)
		view.showProgress()
	}
}
