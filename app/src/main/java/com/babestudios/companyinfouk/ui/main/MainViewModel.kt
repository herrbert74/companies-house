package com.babestudios.companyinfouk.ui.main

import com.babestudios.base.mvp.StateViewModel

class MainViewModel : StateViewModel<SearchState>(SearchState(ArrayList(), null))