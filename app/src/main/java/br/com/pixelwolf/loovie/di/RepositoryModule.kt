package br.com.pixelwolf.loovie.di

import br.com.pixelwolf.loovie.repository.IMovieRepository
import br.com.pixelwolf.loovie.repository.MovieRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<IMovieRepository> { MovieRepository(get()) }

}