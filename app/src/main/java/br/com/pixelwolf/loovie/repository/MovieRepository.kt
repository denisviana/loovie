package br.com.pixelwolf.loovie.repository

import br.com.pixelwolf.loovie.api.RestApiService
import br.com.pixelwolf.loovie.api.response.ErrorClass
import br.com.pixelwolf.loovie.api.response.MoviesResponse
import br.com.pixelwolf.loovie.model.Movie
import retrofit2.Converter
import retrofit2.Response

class MovieRepository(
    val api : RestApiService
) : IMovieRepository {

    override suspend fun getUpcomingMovies(
        language : String,
        page : Int,
        region : String): Response<MoviesResponse> = api.getUpcomingMovies(lang = language, page = page, region = region)

    override suspend fun getMovieById(
        movieId : Int,
        language : String
    ): Response<Movie> = api.getMovieById(lang = language, movieId = movieId)

    override suspend fun searchMovies(
        lang: String,
        region: String,
        query: String
    ): Response<MoviesResponse> = api.searchMovies(lang = lang, region = region, query = query)

}