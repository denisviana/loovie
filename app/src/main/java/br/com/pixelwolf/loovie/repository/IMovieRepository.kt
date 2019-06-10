package br.com.pixelwolf.loovie.repository

import br.com.pixelwolf.loovie.api.response.MoviesResponse
import br.com.pixelwolf.loovie.model.Movie
import retrofit2.Response

interface IMovieRepository {

    suspend fun getUpcomingMovies(
        apiKey : String,
        language : String,
        page : Int,
        region : String
    ) : Response<MoviesResponse>

    suspend fun getMovieById(
        apiKey : String,
        movieId : Int,
        language : String
    ) : Response<Movie>

    suspend fun searchMovies(
        api_key: String,
        lang: String,
        region : String,
        query : String
    ) : Response<MoviesResponse>
}