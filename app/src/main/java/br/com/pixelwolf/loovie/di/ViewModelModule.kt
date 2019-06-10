package br.com.pixelwolf.loovie.di

import br.com.pixelwolf.loovie.ui.main.MainViewModel
import br.com.pixelwolf.loovie.ui.movie_details.MovieDetailsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { MainViewModel( get ()) }
    viewModel { MovieDetailsViewModel(get ()) }

}