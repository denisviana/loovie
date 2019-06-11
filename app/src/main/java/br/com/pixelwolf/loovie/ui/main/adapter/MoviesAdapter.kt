package br.com.pixelwolf.loovie.ui.main.adapter

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.view.ViewCompat
import br.com.pixelwolf.loovie.R
import br.com.pixelwolf.loovie.model.Movie
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class MoviesAdapter(movies : MutableList<Movie>) : BaseQuickAdapter<Movie, BaseViewHolder>(R.layout.layout_movie_item,movies)  {

    override fun convert(helper: BaseViewHolder?, item: Movie?) {

        val sdfIn = SimpleDateFormat("yyyy-MM-dd", Locale("pt-BR"))
        val sdfOut = SimpleDateFormat("dd/MM/yyyy", Locale("pt-BR"))

        if(item?.releaseDate?.isNotEmpty()!!){
            val date = sdfIn.parse(item.releaseDate)
            val releaseDate = sdfOut.format(date)

            helper?.getView<TextView>(R.id.movie_release_date)!!.visibility = VISIBLE
            helper.setText(R.id.movie_release_date,releaseDate)

        }else helper?.getView<TextView>(R.id.movie_release_date)!!.visibility = GONE

        Glide.with(mContext)
            .load("https://image.tmdb.org/t/p/w154/${item.posterPath}")
            .centerCrop()
            .into(helper.getView(R.id.movie_poster)!!)

        ViewCompat.setTransitionName(helper.getView(R.id.movie_poster), item.title)

        helper.setText(R.id.movie_title, item.title)
        helper.setText(R.id.movie_vote_average, "${item.voteAverage.toString()}/10")

    }
}