package com.babestudios.companyinfouk.ui.persons

import android.support.annotation.VisibleForTesting
import android.util.Log

import com.babestudios.companyinfouk.data.DataManager
import com.babestudios.companyinfouk.data.model.persons.Persons

import net.grandcentrix.thirtyinch.TiPresenter

import javax.inject.Inject

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

class PersonsPresenter @Inject
constructor(internal var dataManager: DataManager) : TiPresenter<PersonsActivityView>(), Observer<Persons> {

	internal var persons: Persons? = null

	override fun onAttachView(view: PersonsActivityView) {
		super.onAttachView(view)
		persons?.let {
			view.showPersons(it)
		} ?: run {
			view.showProgress()
			getPersons()
		}
	}

	@VisibleForTesting
	fun getPersons() {
		view?.let {
			dataManager.getPersons(it.companyNumber, "0").subscribe(this)
		}
	}

	override fun onComplete() {}

	override fun onSubscribe(d: Disposable) {

	}

	override fun onError(e: Throwable) {
		view?.let {
			it.hideProgress()
			Log.d("test", "onError: " + e.fillInStackTrace())
			if (e is HttpException) {
				Log.d("test", "onError: " + e.code())
				if (e.code() == 404) {
					it.showNoPersons()
				} else {
					it.showError()
				}
			} else {
				it.showError()
			}
		}
	}

	override fun onNext(persons: Persons) {
		view?.let {
			it.hideProgress()
			it.showPersons(persons)
		}
	}

}
