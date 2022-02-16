package com.babestudios.companyinfouk.persons.ui.persons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.base.mvrx.BaseFragment
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.VIEW_STATE_EMPTY
import com.babestudios.base.view.MultiStateView.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.persons.databinding.FragmentPersonsBinding
import com.babestudios.companyinfouk.persons.ui.PersonsActivity
import com.babestudios.companyinfouk.persons.ui.PersonsViewModel
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsAdapter
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsTypeFactory
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsViewHolder
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsVisitableBase
import io.reactivex.disposables.CompositeDisposable

class PersonsFragment : BaseFragment() {

	private var personsAdapter: PersonsAdapter? = null

	private val viewModel by activityViewModel(PersonsViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentPersonsBinding? = null
	private val binding get() = _binding!!

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		_binding = FragmentPersonsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabPersons.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabPersons.setNavigationOnClickListener { activity?.onBackPressed() }
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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvPersons.layoutManager = linearLayoutManager
		binding.rvPersons.addItemDecoration(DividerItemDecoration(requireContext()))
		binding.rvPersons.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				viewModel.loadMorePersons(page)
			}
		})
	}

	override fun orientationChanged() {
		val activity = requireActivity() as PersonsActivity
		viewModel.setNavigator(activity.injectPersonsNavigator())
	}

	//endregion

	//region render

	//endregion

	//region events

	private fun observeActions() {
		personsAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<PersonsVisitableBase> ->
					viewModel.personItemClicked((view as PersonsViewHolder).adapterPosition)
				}
				?.let { eventDisposables.add(it) }
	}

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.personsRequest) {
				is Loading -> binding.msvPersons.viewState = VIEW_STATE_LOADING
				is Fail -> {
					binding.msvPersons.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvPersons.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.personsRequest.error.message
				}
				is Success -> {
					if (state.persons.isEmpty()) {
						binding.msvPersons.viewState = VIEW_STATE_EMPTY
					} else {
						binding.msvPersons.viewState = VIEW_STATE_CONTENT
						if (binding.rvPersons.adapter == null) {
							personsAdapter = PersonsAdapter(state.persons, PersonsTypeFactory())
							binding.rvPersons.adapter = personsAdapter
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
