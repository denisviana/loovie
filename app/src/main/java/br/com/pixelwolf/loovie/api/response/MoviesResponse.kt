package br.com.pixelwolf.loovie.api.response

import br.com.pixelwolf.loovie.model.Movie
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("results")
    val result : List<Movie>? = null
)