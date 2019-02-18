package com.babestudios.base.view

import android.content.Context
import android.graphics.drawable.Drawable
import com.google.android.material.appbar.AppBarLayout
import androidx.appcompat.widget.Toolbar
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import com.babestudios.companyinfouk.R
import kotlinx.android.synthetic.main.base_view_parallax_app_bar.view.*


class ParallaxAppBarView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
	: AppBarLayout(context, attrs) {
	init {
		val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		inflater.inflate(R.layout.base_view_parallax_app_bar, this)

		val ivParallaxAppBar: ImageView = findViewById(R.id.ivParallaxAppBar)

		val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParallaxAppBarView)
		ivParallaxAppBar.setImageDrawable(typedArray.getDrawable(R.styleable.ParallaxAppBarView_imageViewSrc))
		typedArray.recycle()
	}

	override fun onFinishInflate() {
		super.onFinishInflate()
		ivParallaxAppBar
	}

	fun setImageViewResource(drawable: Drawable) {
		ivParallaxAppBar.setImageDrawable(drawable)
	}

	fun setTitle(title: CharSequence) {
		tbParallaxAppBar.title = title
	}

	fun getToolbar(): Toolbar {
		return tbParallaxAppBar
	}

	fun setNavigationOnClickListener(lambda: () -> Unit) {
		tbParallaxAppBar.setNavigationOnClickListener { lambda() }
	}
}