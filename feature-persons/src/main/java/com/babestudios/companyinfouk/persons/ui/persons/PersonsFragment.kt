package com.babestudios.companyinfouk.persons.ui.persons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.view.MviView
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.internal.PublishSubject
import com.babestudios.base.ext.viewBinding
import com.babestudios.base.network.OfflineException
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_EMPTY
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.navigation.navigateSafe
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.persons.databinding.FragmentPersonsBinding
import com.babestudios.companyinfouk.persons.ui.PersonsViewModel
import com.babestudios.companyinfouk.persons.ui.PersonsViewModelFactory
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.State
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PersonsFragment : Fragment(R.layout.fragment_persons), MviView<State, UserIntent> {

	@Inject
	lateinit var personsViewModelFactory: PersonsViewModelFactory

	private val args: PersonsFragmentArgs by navArgs()

	private var personsAdapter: PersonsAdapter? = null

	private lateinit var personsComponent: PersonsComponent

	private val viewModel: PersonsViewModel by viewModels {
		PersonsViewModel.provideFactory(
			personsViewModelFactory,
			args.selectedCompanyId
		)
	}

	private val binding by viewBinding<FragmentPersonsBinding>()

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

	fun sideEffects(sideEffect: SideEffect) {
		when (sideEffect) {
			is SideEffect.PersonsItemClicked ->
				findNavController().navigateSafe(
					PersonsFragmentDirections.actionToDetails(sideEffect.selectedPerson)
				)
		}
	}

	/**
	 * Dispatches the provided `View Event` to all subscribers
	 *
	 * @param event a `View Event` to be dispatched
	 */
	@MainThread
	fun dispatch(event: UserIntent) {
		subject.onNext(event)
	}

	override fun render(model: State) {
		when (model) {
			is State.Loading -> binding.msvPersons.viewState = VIEW_STATE_LOADING
			is State.Error -> {
				if (model.t is OfflineException) {
					binding.msvPersons.viewState = VIEW_STATE_EMPTY
				} else {
					binding.msvPersons.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvPersons.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = model.t.message
				}
			}
			is State.Show -> {
				binding.msvPersons.viewState = VIEW_STATE_CONTENT
				if (binding.rvPersons.adapter == null) {
					personsAdapter = PersonsAdapter(model.personsResponse.items, lifecycleScope)
					binding.rvPersons.adapter = personsAdapter
				} else {
					personsAdapter?.updateItems(model.personsResponse.items)
				}
				personsAdapter?.itemClicks?.onEach {
					dispatch(UserIntent.PersonsItemClicked(it))
				}?.launchIn(lifecycleScope)
			}
		}
	}

	//endregion

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		personsComponent = PersonsComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null)
		)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return ComposeView(requireContext()).apply {
			setContent {
				PersonsContent(personsComponent)
			}
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.onViewCreated(this, essentyLifecycle())
		initializeToolBar()
		createRecyclerView()
	}

	private fun initializeToolBar() {
		(activity as AppCompatActivity).setSupportActionBar(binding.pabPersons.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabPersons.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.persons_with_significant_control)
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvPersons.layoutManager = linearLayoutManager
		binding.rvPersons.addItemDecoration(DividerItemDecoration(requireContext()))
		binding.rvPersons.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				dispatch(UserIntent.LoadMorePersons(page))
			}
		})
	}

	//endregion

}

sealed class UserIntent {
	data class PersonsItemClicked(val selectedPerson: Person) : UserIntent()
	data class LoadMorePersons(val page: Int) : UserIntent()
}

sealed class SideEffect {
	data class PersonsItemClicked(val selectedPerson: Person) : SideEffect()
}

@Composable
fun PersonsContent(component: PersonsComponent) {
	val stack = component.childStack.subscribeAsState()
	Children(stack = stack, animation = scale()) {
		when (val child = it.instance) {
			is PersonsChild.Main -> TodoMainContent(child.component)
			is PersonsChild.Edit -> TodoEditContent(child.component)
		}
	}
}
