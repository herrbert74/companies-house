package com.babestudios.companyinfouk.ui.insolvency

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfo.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.insolvency.list.AbstractInsolvencyVisitable
import com.babestudios.companyinfouk.ui.insolvency.list.InsolvencyAdapter
import com.babestudios.companyinfouk.ui.insolvency.list.InsolvencyTypeFactory
import com.babestudios.companyinfouk.ui.insolvency.list.InsolvencyViewHolder
import com.babestudios.companyinfouk.ui.insolvencydetails.createInsolvencyDetailsIntent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_insolvency.*
import kotlinx.android.synthetic.main.multi_state_view_error.view.*

private const val COMPANY = "com.babestudios.companyinfouk.ui.company"

class InsolvencyActivity : RxAppCompatActivity(), ScopeProvider {


	private var insolvencyAdapter: InsolvencyAdapter? = null

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(InsolvencyViewModel::class.java) }

	private lateinit var insolvencyPresenter: InsolvencyPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private lateinit var comp: InsolvencyComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_insolvency)
		logScreenView(this.localClassName)
		setSupportActionBar(pabInsolvency.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabInsolvency.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.insolvency)
		when {
			viewModel.state.value.insolvencyItems != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<InsolvencyState>("STATE")?.let {
					with(viewModel.state.value) {
						insolvencyItems = it.insolvencyItems
						companyNumber = it.companyNumber
					}
				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value.companyNumber = intent.getStringExtra(COMPANY)
				initPresenter(viewModel)
			}
		}

		createRecyclerView()
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

	private fun initPresenter(viewModel: InsolvencyViewModel) {
		if (!::insolvencyPresenter.isInitialized) {
			comp = DaggerInsolvencyComponent
					.builder()
					.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
					.build()
			insolvencyPresenter = comp.insolvencyPresenter()
			insolvencyPresenter.setViewModel(viewModel, requestScope())
		}
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvInsolvency?.layoutManager = linearLayoutManager
		rvInsolvency.addItemDecoration(DividerItemDecoration(this))
	}

	override fun onBackPressed() {
		super.finish()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	//endregion

	//region render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: InsolvencyState) {
		when {
			state.isLoading -> msvInsolvency.viewState = VIEW_STATE_LOADING
			state.errorType != ErrorType.NONE -> {
				msvInsolvency.viewState = VIEW_STATE_ERROR
				state.errorType = ErrorType.NONE
				msvInsolvency.tvMsvError.text = state.errorMessage
			}
			state.insolvencyItems == null -> msvInsolvency.viewState = VIEW_STATE_EMPTY
			else -> {
				state.insolvencyItems?.let {
					msvInsolvency.viewState = VIEW_STATE_CONTENT
					if (rvInsolvency?.adapter == null) {
						insolvencyAdapter = InsolvencyAdapter(it, InsolvencyTypeFactory())
						rvInsolvency?.adapter = insolvencyAdapter
					} else {
						insolvencyAdapter?.updateItems(it)
					}
					observeActions()
				}
			}
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		insolvencyAdapter?.getViewClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractInsolvencyVisitable> ->
					viewModel.state.value.insolvencyItems?.let { insolvencyItems ->
						startActivityWithRightSlide(
								this.createInsolvencyDetailsIntent(insolvencyItems[(view as InsolvencyViewHolder).adapterPosition].insolvencyCase))
					}
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion
}

fun Context.createInsolvencyIntent(company: String): Intent {
	return Intent(this, InsolvencyActivity::class.java)
			.putExtra(COMPANY, company)
}
