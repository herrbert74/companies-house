package com.babestudios.companyinfouk.ui.charges

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.babestudios.base.ext.biLet
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.chargedetails.createChargeDetailsIntent
import com.babestudios.companyinfouk.ui.charges.list.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_charges.*
import kotlinx.android.synthetic.main.multi_state_view_error.*

private const val COMPANY_NUMBER = "com.babestudios.companyinfouk.ui.COMPANY_NUMBER"

class ChargesActivity : RxAppCompatActivity(), ScopeProvider {

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(ChargesViewModel::class.java) }

	private var chargesAdapter: ChargesAdapter? = null

	private lateinit var chargesPresenter: ChargesPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private lateinit var comp: ChargesComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_charges)
		logScreenView(this.localClassName)
		setSupportActionBar(pabCharges.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabCharges.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.charges)
		createRecyclerView()
		when {
			viewModel.state.value?.chargeItems != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				(savedInstanceState.getParcelable<ChargesState>("STATE") to viewModel.state.value)
						.biLet { savedState, state ->
						state.companyNumber = savedState.companyNumber

				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value?.companyNumber = intent.getStringExtra(COMPANY_NUMBER)!!
				initPresenter(viewModel)
			}
		}
		observeState()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putParcelable("STATE", viewModel.state.value)
		super.onSaveInstanceState(outState)
	}

	private fun initPresenter(viewModel: ChargesViewModel) {
		val maybePresenter = lastCustomNonConfigurationInstance as ChargesPresenterContract?

		if (maybePresenter != null) {
			chargesPresenter = maybePresenter
		}

		if (!::chargesPresenter.isInitialized) {
			comp = DaggerChargesComponent
					.builder()
					.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
					.build()
			chargesPresenter = comp.chargesPresenter()
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

	override fun onBackPressed() {
		super.finish()
		overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out)
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
				state.errorType = ErrorType.NONE
				tvMsvError.text = getString(R.string.could_not_retrieve_charges_info)
			}
			state.chargeItems == null -> msvCharges.viewState = VIEW_STATE_EMPTY
			else -> {
				state.chargeItems?.let {
					msvCharges.viewState = VIEW_STATE_CONTENT
					if (rvCharges?.adapter == null) {
						chargesAdapter = ChargesAdapter(it, ChargesTypeFactory())
						rvCharges?.adapter = chargesAdapter
						observeActions()
					} else {
						chargesAdapter?.updateItems(it)
						observeActions()
					}
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
					viewModel.state.value?.chargeItems?.let { chargeItems ->
						startActivityWithRightSlide(this.createChargeDetailsIntent(
								(chargeItems[(view as ChargesViewHolder).adapterPosition] as ChargesVisitable).chargesItem))
					}
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion
}

fun Context.createChargesIntent(companyNumber: String): Intent {
	return Intent(this, ChargesActivity::class.java)
			.putExtra(COMPANY_NUMBER, companyNumber)
}
