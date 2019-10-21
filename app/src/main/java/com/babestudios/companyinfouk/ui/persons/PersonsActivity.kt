package com.babestudios.companyinfouk.ui.persons

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
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.persondetails.createPersonDetailsIntent
import com.babestudios.companyinfouk.ui.persons.list.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_persons.*
import kotlinx.android.synthetic.main.multi_state_view_error.view.*

private const val COMPANY_NUMBER = "com.babestudios.companyinfouk.ui.company_number"

class PersonsActivity : RxAppCompatActivity(), ScopeProvider {

	private var personsAdapter: PersonsAdapter? = null

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(PersonsViewModel::class.java) }

	private lateinit var personsPresenter: PersonsPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private lateinit var comp: PersonsComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_persons)
		logScreenView(this.localClassName)
		setSupportActionBar(pabPersons.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabPersons.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.persons_with_significant_control)
		when {
			viewModel.state.value?.persons != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				(savedInstanceState.getParcelable<PersonsState>("STATE") to viewModel.state.value)
						.biLet { savedState, state ->
							state.persons = savedState.persons
							state.companyNumber = savedState.companyNumber
						}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value?.companyNumber = intent.getStringExtra(COMPANY_NUMBER)
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

	private fun initPresenter(viewModel: PersonsViewModel) {
		if (!::personsPresenter.isInitialized) {
			comp = DaggerPersonsComponent
					.builder()
					.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
					.build()
			personsPresenter = comp.personsPresenter()
			personsPresenter.setViewModel(viewModel, requestScope())
		}
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvPersons?.layoutManager = linearLayoutManager
		rvPersons.addItemDecoration(DividerItemDecoration(this))
		rvPersons.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				personsPresenter.loadMorePersons(page)
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

	private fun render(state: PersonsState) {
		when {
			state.isLoading -> msvPersons.viewState = VIEW_STATE_LOADING
			state.errorType != ErrorType.NONE -> {
				msvPersons.viewState = VIEW_STATE_ERROR
				state.errorType = ErrorType.NONE
				msvPersons.tvMsvError.text = state.errorMessage
			}
			state.persons == null -> msvPersons.viewState = VIEW_STATE_EMPTY
			else -> {
				state.persons?.let {
					msvPersons.viewState = VIEW_STATE_CONTENT
					if (rvPersons?.adapter == null) {
						personsAdapter = PersonsAdapter(it, PersonsTypeFactory())
						rvPersons?.adapter = personsAdapter
					} else {
						personsAdapter?.updateItems(it)
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
		personsAdapter?.getViewClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractPersonsVisitable> ->
					viewModel.state.value?.persons?.let { personItems ->
						startActivityWithRightSlide(
								this.createPersonDetailsIntent(
										(personItems[(view as Persons2ViewHolder).adapterPosition] as PersonsVisitable).person))
					}
				}
				?.let { eventDisposables.add(it) }
	}

	override fun onBackPressed() {
		super.finish()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	//endregion
}

fun Context.createPersonsIntent(companyNumber: String): Intent {
	return Intent(this, PersonsActivity::class.java)
			.putExtra(COMPANY_NUMBER, companyNumber)
}
