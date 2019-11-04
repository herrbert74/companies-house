package com.babestudios.companyinfouk.insolvencies.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsAdapter
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsTypeFactory
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_insolvency_details.*
import java.util.ArrayList

private const val INSOLVENCY_CASE = "com.babestudios.companyinfouk.ui.insolvency_case"

class InsolvencyDetailsFragment : RxAppCompatActivity(), ScopeProvider {


	private var insolvencyDetailsAdapter: InsolvencyDetailsAdapter? = null

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(InsolvencyDetailsViewModel::class.java) }

	private lateinit var insolvencyDetailsPresenter: InsolvencyDetailsPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private lateinit var comp: InsolvencyDetailsComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_insolvency_details)
		logScreenView(this.localClassName)
		setSupportActionBar(pabInsolvencyDetails.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabInsolvencyDetails.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.insolvency_details)
		when {
			viewModel.state.value?.insolvencyDetailsItems != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				(savedInstanceState.getParcelable<InsolvencyDetailsState>("STATE") to viewModel.state.value)
						.biLet { savedState, state ->
						state.insolvencyDetailsItems = savedState.insolvencyDetailsItems
						state.insolvencyCase = savedState.insolvencyCase
				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value?.insolvencyCase = intent.getParcelableExtra(INSOLVENCY_CASE)
				initPresenter(viewModel)
			}
		}

		createRecyclerView()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
		observeState()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putParcelable("STATE", viewModel.state.value)
		super.onSaveInstanceState(outState)
	}

	private fun initPresenter(viewModel: InsolvencyDetailsViewModel) {
		if (!::insolvencyDetailsPresenter.isInitialized) {
			comp = DaggerInsolvencyDetailsComponent
					.builder()
					.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
					.build()
			insolvencyDetailsPresenter = comp.insolvencyDetailsPresenter()
			insolvencyDetailsPresenter.setViewModel(viewModel, requestScope())
		}
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvInsolvencyDetails?.layoutManager = linearLayoutManager
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

	private fun render(state: InsolvencyDetailsState) {
		when {
			state.isLoading -> msvInsolvencyDetails.viewState = VIEW_STATE_LOADING
			state.errorType != ErrorType.NONE -> {
				msvInsolvencyDetails.viewState = VIEW_STATE_ERROR
				state.errorType = ErrorType.NONE
				msvInsolvencyDetails.tvMsvError.text = state.errorMessage
			}
			state.insolvencyDetailsItems?.isEmpty() == true -> msvInsolvencyDetails.viewState = VIEW_STATE_EMPTY
			else -> {
				state.insolvencyDetailsItems?.let { items ->
					msvInsolvencyDetails.viewState = VIEW_STATE_CONTENT
					if (rvInsolvencyDetails?.adapter == null) {
						val titlePositions = ArrayList<Int>()
						titlePositions.add(0)
						titlePositions.add(viewModel.state.value?.insolvencyCase?.dates?.size.let { it } ?: 0 + 1)
						rvInsolvencyDetails.addItemDecoration(
								DividerItemDecorationWithSubHeading(this, titlePositions))
						insolvencyDetailsAdapter = InsolvencyDetailsAdapter(items, InsolvencyDetailsTypeFactory())
						rvInsolvencyDetails?.adapter = insolvencyDetailsAdapter
						observeActions()
					} else {
						insolvencyDetailsAdapter?.updateItems(items)
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
	}

	//endregion
}

fun Context.createInsolvencyDetailsIntent(insolvencyCase: InsolvencyCase): Intent {
	return Intent(this, InsolvencyDetailsFragment::class.java)
			.putExtra(INSOLVENCY_CASE, insolvencyCase)
}
