package com.babestudios.companyinfouk.persons.ui.persons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.persons.ui.PersonsViewModel
import com.babestudios.companyinfouk.persons.ui.persons.list.AbstractPersonsVisitable
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsAdapter
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsTypeFactory
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_persons.*

class PersonsFragment : BaseMvRxFragment() {

	private var personsAdapter: PersonsAdapter? = null

	private val viewModel by activityViewModel(PersonsViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

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

	//endregion

	//region events

	private fun observeActions() {
		personsAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<AbstractPersonsVisitable> ->
					viewModel.personItemClicked((view as PersonsViewHolder).adapterPosition)
				}
				?.let { eventDisposables.add(it) }
	}

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.personsRequest) {
				is Loading -> msvPersons.viewState = VIEW_STATE_LOADING
				is Fail -> {
					msvPersons.viewState = VIEW_STATE_ERROR
					val tvMsvError = msvPersons.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.personsRequest.error.message
				}
				is Success -> {
					if (state.persons.isEmpty()) {
						msvPersons.viewState = VIEW_STATE_EMPTY
					} else {
						msvPersons.viewState = VIEW_STATE_CONTENT
						if (rvPersons?.adapter == null) {
							personsAdapter = PersonsAdapter(state.persons, PersonsTypeFactory())
							rvPersons?.adapter = personsAdapter
						} else {
							personsAdapter?.updateItems(state.persons)
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
