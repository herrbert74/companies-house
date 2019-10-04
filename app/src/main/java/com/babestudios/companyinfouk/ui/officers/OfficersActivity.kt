package com.babestudios.companyinfouk.ui.officers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfo.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.officerdetails.createOfficerDetailsIntent
import com.babestudios.companyinfouk.ui.officers.list.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_officers.*
import kotlinx.android.synthetic.main.multi_state_view_error.view.*

private const val COMPANY_NUMBER = "com.babestudios.companyinfouk.ui.company_number"

class OfficersActivity : RxAppCompatActivity(), ScopeProvider {


	private var officersAdapter: OfficersAdapter? = null

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(OfficersViewModel::class.java) }

	private lateinit var officersPresenter: OfficersPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private lateinit var comp: OfficersComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_officers)
		logScreenView(this.localClassName)
		setSupportActionBar(pabOfficers.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabOfficers.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.officers)
		when {
			viewModel.state.value.officerItems != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<OfficersState>("STATE")?.let {
					with(viewModel.state.value) {
						officerItems = it.officerItems
						companyNumber = it.companyNumber
					}
				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value.companyNumber = intent.getStringExtra(COMPANY_NUMBER)
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

	private fun initPresenter(viewModel: OfficersViewModel) {
		if (!::officersPresenter.isInitialized) {
			comp = DaggerOfficersComponent
					.builder()
					.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
					.build()
			officersPresenter = comp.officersPresenter()
			officersPresenter.setViewModel(viewModel, requestScope())
		}
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvOfficers?.layoutManager = linearLayoutManager
		rvOfficers.addItemDecoration(DividerItemDecoration(this))
		rvOfficers.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				officersPresenter.loadMoreOfficers(page)
			}
		})
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

	private fun render(state: OfficersState) {
		when {
			state.isLoading -> msvOfficers.viewState = VIEW_STATE_LOADING
			state.errorType != ErrorType.NONE -> {
				msvOfficers.viewState = VIEW_STATE_ERROR
				state.errorType = ErrorType.NONE
				msvOfficers.tvMsvError.text = state.errorMessage
			}
			state.officerItems == null -> msvOfficers.viewState = VIEW_STATE_EMPTY
			else -> {
				state.officerItems?.let {
					msvOfficers.viewState = VIEW_STATE_CONTENT
					if (rvOfficers?.adapter == null) {
						officersAdapter = OfficersAdapter(it, OfficersTypeFactory())
						rvOfficers?.adapter = officersAdapter
					} else {
						officersAdapter?.updateItems(it)
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
		officersAdapter?.getViewClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractOfficersVisitable> ->
					viewModel.state.value.officerItems?.let { officerItems ->
						startActivityWithRightSlide(
								this.createOfficerDetailsIntent(
										(officerItems[(view as OfficersViewHolder).adapterPosition] as OfficersVisitable).officersItem))
					}
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion
}

fun Context.createOfficersIntent(companyNumber: String): Intent {
	return Intent(this, OfficersActivity::class.java)
			.putExtra(COMPANY_NUMBER, companyNumber)
}
