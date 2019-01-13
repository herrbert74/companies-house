package com.babestudios.base.mvp.list

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<in T>(view: View): RecyclerView.ViewHolder(view) {
	abstract fun bind(visitable: T)
}