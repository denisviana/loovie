package br.com.pixelwolf.loovie.api.response

import com.google.gson.annotations.SerializedName

data class ErrorClass(
    @SerializedName("status_message")
    val statusMessage : String,
    @SerializedName("status_code")
    val statusCode : String
)