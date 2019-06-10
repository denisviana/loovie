package br.com.pixelwolf.loovie.repository

import br.com.pixelwolf.loovie.api.RestApiService
import br.com.pixelwolf.loovie.api.response.MoviesResponse
import br.com.pixelwolf.loovie.model.Movie
import retrofit2.Response

class MovieRepository(
    val api : RestApiService
) : IMovieRepository {

    override suspend fun getUpcomingMovies(
        apiKey : String,
        language : String,
        page : Int,
        region : String): Response<MoviesResponse> = api.getUpcomingMovies(api_key = apiKey, lang = language, page = page, region = region)

    override suspend fun getMovieById(
        apiKey : String,
        movieId : Int,
        language : String
    ): Response<Movie> = api.getMovieById(api_key = apiKey, lang = language, movieId = movieId)

    override suspend fun searchMovies(
        api_key: String,
        lang: String,
        region: String,
        query: String
    ): Response<MoviesResponse> = api.searchMovies(api_key = api_key, lang = lang, region = region, query = query)

}