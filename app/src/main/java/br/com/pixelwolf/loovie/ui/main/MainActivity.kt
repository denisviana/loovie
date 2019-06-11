package br.com.pixelwolf.loovie.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import br.com.pixelwolf.loovie.R
import br.com.pixelwolf.loovie.model.Movie
import br.com.pixelwolf.loovie.ui.main.adapter.MoviesAdapter
import br.com.pixelwolf.loovie.ui.movie_details.MovieDetailsActivity
import br.com.pixelwolf.loovie.ui.util.CustomLoadingMoreView
import br.com.pixelwolf.loovie.ui.util.PaginationScrollListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    val viewModel : MainViewModel by inject()
    val adapter = MoviesAdapter(mutableListOf())

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var loading : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTransition()
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(this,VERTICAL,false)
        rv_movies.layoutManager = layoutManager
        rv_movies.adapter = adapter

        adapter.setLoadMoreView(CustomLoadingMoreView())

        adapter.setOnItemClickListener { adapter, view, position ->
            val movie = adapter.data[position] as Movie
            val sharedView = view.findViewById<ImageView>(R.id.movie_poster)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedView,
                "poster")
            startActivity(MovieDetailsActivity.createIntent(this,movie), options.toBundle())
        }

        rv_movies.addOnScrollListener(object : PaginationScrollListener(layoutManager){
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                loading = LayoutInflater.from(this@MainActivity).inflate(R.layout.loading_view,null)
                adapter.addFooterView(loading)
                viewModel.getUpComingMovies()
            }

        })

        viewModel.state.observe(this, Observer {

            when(it){
                is MainViewModel.MoviesState.Loading -> {
                    loading_movies.visibility = VISIBLE
                }
                is MainViewModel.MoviesState.Empty -> {
                    loading_movies.visibility = GONE
                    isLastPage = true
                    if(loading!=null )
                        adapter.removeFooterView(loading)
                }
                is MainViewModel.MoviesState.Error -> {
                    loading_movies.visibility = GONE
                    error_movies.visibility = VISIBLE
                    error_message.text = it.message
                }
                is MainViewModel.MoviesState.Success -> {
                    loading_movies.visibility = GONE
                    adapter.addData(it.movies)
                    isLoading = false
                    if(loading!=null )
                        adapter.removeFooterView(loading)
                }
                is MainViewModel.MoviesState.SearchSuccess -> {
                    loading_movies.visibility = GONE
                    adapter.replaceData(it.movies)
                }
                is MainViewModel.MoviesState.ResetSearch -> {
                    adapter.data.clear()
                    adapter.notifyDataSetChanged()
                    viewModel.getUpComingMovies()
                }
            }

        })

        viewModel.getUpComingMovies()

    }
    private fun initTransition(){
        with(window){
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.END)

            exitTransition = Slide(Gravity.START)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem =  menu?.findItem(R.id.search_action)
        searchItem?.expandActionView()
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(
            DebouncingQueryTextListener(
                this@MainActivity.lifecycle
            ) { newText ->
                newText?.let {
                    if (it.isEmpty()) {
                        viewModel.resetSearch()
                    } else {
                        viewModel.searchMovies(it)
                    }
                }
            }
        )

        return true
    }

}

internal class DebouncingQueryTextListener(
    lifecycle: Lifecycle,
    private val onDebouncingQueryTextChange: (String?) -> Unit
) : SearchView.OnQueryTextListener, LifecycleObserver {
    var debouncePeriod: Long = 500

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private var searchJob: Job? = null

    init {
        lifecycle.addObserver(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            newText?.let {
                delay(debouncePeriod)
                onDebouncingQueryTextChange(newText)
            }
        }
        return false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        searchJob?.cancel()
    }
}
