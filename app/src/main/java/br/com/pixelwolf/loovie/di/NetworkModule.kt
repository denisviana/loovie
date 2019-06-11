package br.com.pixelwolf.loovie.di

import br.com.pixelwolf.loovie.api.RestApiService
import br.com.pixelwolf.loovie.api.response.ErrorClass
import br.com.pixelwolf.loovie.api.util.ApiConst.HOST
import br.com.pixelwolf.loovie.api.util.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {

    single { createOkHttpClient() }
    single { provideRetrofit(get(), HOST) }
    single { provideRestApiService(get()) }
    factory { provideConverter( get() ) }
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(AuthInterceptor())
        .addInterceptor(httpLoggingInterceptor).build()
}

fun provideRestApiService(retrofit: Retrofit): RestApiService {
    return retrofit.create<RestApiService>(RestApiService::class.java)
}

fun provideRetrofit(okHttpClient: OkHttpClient, url: String) : Retrofit{
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideConverter(retrofit: Retrofit) : Converter<ResponseBody,ErrorClass>{
    return retrofit.responseBodyConverter<ErrorClass>(ErrorClass::class.java, arrayOf())
}
