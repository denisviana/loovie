package br.com.pixelwolf.loovie.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(@SerializedName("overview")
                 var overview: String? = null,
                 @SerializedName("original_language")
                 var originalLanguage: String? = null,
                 @SerializedName("original_title")
                 var originalTitle: String? = null,
                 @SerializedName("video")
                 var video: Boolean? = null,
                 @SerializedName("title")
                 var title: String? = null,
                 @SerializedName("poster_path")
                 var posterPath: String? = null,
                 @SerializedName("backdrop_path")
                 var backdropPath: String? = null,
                 @SerializedName("release_date")
                 var releaseDate: String? = null,
                 @SerializedName("vote_average")
                 var voteAverage: Double? = null,
                 @SerializedName("popularity")
                 var popularity: Double? = null,
                 @SerializedName("id")
                 var id: Int? = null,
                 @SerializedName("adult")
                 var adult: Boolean? = null,
                 @SerializedName("vote_count")
                 var voteCount: Int? = null,
                 @SerializedName("genres")
                 var genres : List<Genre>? = null,
                 @SerializedName("runtime")
                 var runtime : Int? = null,
                 @SerializedName("budget")
                 var budget : Int? = null,
                 @SerializedName("revenue")
                 var revenue : Int? = null
) : Parcelable