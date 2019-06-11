package br.com.pixelwolf.loovie.api

import br.com.pixelwolf.loovie.api.response.MoviesResponse
import br.com.pixelwolf.loovie.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApiService {

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") lang: String,
        @Query("region") region : String,
        @Query("page") page: Int
    ): Response<MoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId : Int,
        @Query("language") lang: String
    ): Response<Movie>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("language") lang: String,
        @Query("region") region : String,
        @Query("query") query : String
    ) : Response<MoviesResponse>
}