package com.babestudios.companyinfouk.ui.charges

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.CompletableSource
import kotlinx.android.synthetic.main.activity_charges.*
import com.babestudios.companyinfouk.ui.chargesdetails.createChargesDetailsIntent
import com.babestudios.companyinfouk.R
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.Injector
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.charges.list.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.multi_state_view_error.*

private const val COMPANY_NUMBER = "com.babestudios.companyinfouk.ui.COMPANY_NUMBER"

class ChargesActivity : RxAppCompatActivity(), ScopeProvider {

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(ChargesViewModel::class.java) }

	private var chargesAdapter: ChargesAdapter? = null

	private lateinit var chargesPresenter: ChargesPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_charges)
		setSupportActionBar(pabCharges.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabCharges.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.charges)
		initPresenter(intent.extras.getString(COMPANY_NUMBER)!!)
		createRecyclerView()
		observeState()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun initPresenter(companyNumber: String) {
		val maybePresenter = lastCustomNonConfigurationInstance as ChargesPresenterContract?

		if (maybePresenter != null) {
			chargesPresenter = maybePresenter
		}

		if (!::chargesPresenter.isInitialized) {
			viewModel.state.value.companyNumber = companyNumber
			chargesPresenter = Injector.get().chargesPresenter()
			chargesPresenter.setViewModel(viewModel, requestScope())
		}
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvCharges?.layoutManager = linearLayoutManager
		rvCharges?.addItemDecoration(DividerItemDecoration(this))
		rvCharges?.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				chargesPresenter.loadMoreCharges(page)
			}
		})
	}

	//endregion

	//region render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: ChargesState) {
		when {
			state.isLoading -> msvCharges.viewState = VIEW_STATE_LOADING
			state.errorType != ErrorType.NONE -> {
				msvCharges.viewState = VIEW_STATE_ERROR
				tvMsvError.text = getString(R.string.could_not_retrieve_charges_info)
			}
			state.chargeItems == null -> msvCharges.viewState = VIEW_STATE_EMPTY
			else -> {
				msvCharges.viewState = VIEW_STATE_CONTENT
				if (rvCharges?.adapter == null) {
					chargesAdapter = ChargesAdapter(viewModel.state.value.chargeItems, ChargesTypeFactory())
					rvCharges?.adapter = chargesAdapter
					observeActions()
				} else {
					chargesAdapter?.updateItems(viewModel.state.value.chargeItems)
					observeActions()
				}
			}
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		chargesAdapter?.getViewClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractChargesVisitable> ->
					startActivityWithRightSlide(
							this.createChargesDetailsIntent(
									(viewModel.state.value.chargeItems[(view as ChargesViewHolder).adapterPosition] as ChargesVisitable).chargesItem))
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion
}

fun Context.createChargesIntent(companyNumber: String): Intent {
	return Intent(this, ChargesActivity::class.java)
			.putExtra(COMPANY_NUMBER, companyNumber)
}
