package br.com.pixelwolf.loovie.ui.util

import br.com.pixelwolf.loovie.R
import com.chad.library.adapter.base.loadmore.LoadMoreView

class CustomLoadingMoreView : LoadMoreView() {

    override fun getLayoutId(): Int = R.layout.loading_view
    override fun getLoadingViewId(): Int = R.id.load_more_loading_view
    override fun getLoadEndViewId(): Int = 0
    override fun getLoadFailViewId(): Int = 0

}