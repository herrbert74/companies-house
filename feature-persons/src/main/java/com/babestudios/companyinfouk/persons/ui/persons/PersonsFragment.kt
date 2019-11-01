package com.babestudios.companyinfouk.persons.ui.persons

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.persons.ui.PersonsComponent
import com.babestudios.companyinfouk.persons.ui.PersonsState
import com.babestudios.companyinfouk.persons.ui.PersonsViewModel
import com.babestudios.companyinfouk.persons.ui.persons.list.*
import kotlinx.android.synthetic.main.fragment_persons.*

class PersonsFragment : BaseMvRxFragment() {

	private var personsAdapter: PersonsAdapter? = null

	private lateinit var comp: PersonsComponent

	private val viewModel by activityViewModel(PersonsViewModel::class)

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {

		return inflater.inflate(R.layout.fragment_persons, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(pabPersons.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		pabPersons.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.persons_with_significant_control)
		createRecyclerView()
		withState(viewModel) {
			viewModel.fetchPersons(it.companyNumber)
		}

		createRecyclerView()
		observeState()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		rvPersons?.layoutManager = linearLayoutManager
		rvPersons.addItemDecoration(DividerItemDecoration(requireContext()))
		rvPersons.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				viewModel.loadMorePersons(page)
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
		/*personsAdapter?.getViewClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractPersonsVisitable> ->
					viewModel.state.value?.persons?.let { personItems ->
						startActivityWithRightSlide(
								this.createPersonDetailsIntent(
										(personItems[(view as Persons2ViewHolder).adapterPosition] as PersonsVisitable).person))
					}
				}
				?.let { eventDisposables.add(it) }*/
	}

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.officersRequest) {
				is Loading -> msvOfficers.viewState = VIEW_STATE_LOADING
				is Fail -> {
					msvOfficers.viewState = VIEW_STATE_ERROR
					val tvMsvError = msvOfficers.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.officersRequest.error.message
				}
				is Success -> {
					if (state.officerItems.isEmpty()) {
						msvOfficers.viewState = VIEW_STATE_EMPTY
					} else {
						msvOfficers.viewState = VIEW_STATE_CONTENT
						if (rvOfficers?.adapter == null) {
							officersAdapter = OfficersAdapter(state.officerItems, OfficersTypeFactory())
							rvOfficers?.adapter = officersAdapter
						} else {
							officersAdapter?.updateItems(state.officerItems)
						}
					}
					observeActions()
				}
				else -> {
				}
			}
		}
	}
	//endregion
}
