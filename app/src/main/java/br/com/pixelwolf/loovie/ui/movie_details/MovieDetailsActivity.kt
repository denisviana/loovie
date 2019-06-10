package br.com.pixelwolf.loovie.ui.movie_details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.pixelwolf.loovie.R
import br.com.pixelwolf.loovie.api.util.ApiConst.BACKDROP_HOST
import br.com.pixelwolf.loovie.api.util.ApiConst.POSTER_HOST
import br.com.pixelwolf.loovie.model.Movie
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie_details.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {

    companion object{
        const val MOVIE = "movie"
        fun createIntent(context : Context, movie : Movie) : Intent{
            return Intent(context, MovieDetailsActivity::class.java).apply {
                putExtra(MOVIE,movie)
            }
        }

    }

    val viewModel : MovieDetailsViewModel by inject()

    private var movie : Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        setSupportActionBar(toolbar)
        title = ""
        if(supportActionBar!=null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        movie = intent.getParcelableExtra(MOVIE) as Movie

        initView()

        viewModel.state.observe(this, Observer {
            when(it){
                is MovieDetailsViewModel.MoviesState.Loading -> {
                    shimmer_header.visibility = VISIBLE
                    shimmer_overview.visibility = VISIBLE
                }
                is MovieDetailsViewModel.MoviesState.Error -> {

                }
                is MovieDetailsViewModel.MoviesState.Success -> {
                    shimmer_header.visibility = GONE
                    shimmer_overview.visibility = GONE
                    updateUi(it.movie)
                }
            }
        })

        viewModel.getMovieDetails(movie?.id!!)

        //details_tag_group.setTags(arrayListOf("Aventura","Animação","Comédia","Família"))

    }

    private fun initView(){

        Glide.with(this)
            .load(Uri.parse("$BACKDROP_HOST${movie?.backdropPath}"))
            .centerCrop()
            .into(backdrop_image)

        Glide.with(this)
            .load(Uri.parse("$POSTER_HOST${movie?.posterPath}"))
            .centerCrop()
            .into(details_poster)

    }

    private fun updateUi(movie: Movie){

        details_title.text = movie.title

        if(movie.releaseDate?.isNotEmpty()!!){

            val sdfIn = SimpleDateFormat("yyyy-MM-dd", Locale("pt-BR"))
            val sdfOut = SimpleDateFormat("dd/MM/yyyy", Locale("pt-BR"))

            val date = sdfIn.parse(movie.releaseDate)
            val releaseDate = sdfOut.format(date)

            details_release_date!!.visibility = VISIBLE
            details_release_date.text = releaseDate

        }else details_release_date!!.visibility = GONE

        details_release_date.text = movie.releaseDate

        if(movie.voteAverage!! > 0){
            details_average.visibility = VISIBLE
            details_average.text = "${movie.voteAverage}/10"
        }

        details_runtime.text = "  |  ${movie.runtime } min"


        if(movie.revenue!= null && movie.budget!! > 0)
            details_budget.text = String.format("US$ %,.2f", movie.budget!!.toFloat())
        else details_budget.visibility = GONE

        if(movie.revenue!= null && movie.revenue!! > 0)
            details_revenue.text = String.format("US$ %,.2f", movie.revenue!!.toFloat())
        else details_revenue.visibility = GONE

        details_tag_group.setTags(
            movie.genres?.map { it.name }
        )

        details_overview_label.text = "Sinopse"
        details_overview.text = movie.overview

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}