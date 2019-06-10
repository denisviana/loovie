package br.com.pixelwolf.loovie.di

import br.com.pixelwolf.loovie.ui.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { MainViewModel(get ()) }

}