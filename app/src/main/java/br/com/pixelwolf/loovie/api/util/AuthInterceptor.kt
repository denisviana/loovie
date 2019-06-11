package br.com.pixelwolf.loovie.api.util

import br.com.pixelwolf.loovie.api.util.ApiConst.API_KEY
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            var request = chain.request()
            val url = request.url.newBuilder().addQueryParameter("api_key",API_KEY).build()
            val builder = request.newBuilder().url(url)
            val response : Response
            request = builder.build()
            response = chain.proceed(request)
            response
        }
    }

}