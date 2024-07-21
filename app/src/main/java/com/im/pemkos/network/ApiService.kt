package com.im.pemkos.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiService
{
    companion object {
//        const val URL: String = "https://pemkosbutik123.my.id/api/"
        const val URL: String = "http://192.168.18.41:8081/pemkos/api/"

        var retrofit: Retrofit? = null


        private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .addInterceptor{
                val original = it.request()
                val requestBuilder = original.newBuilder()
                val request = requestBuilder.method(original.method, original.body).build()
                return@addInterceptor it.proceed(request)
            }
            .build()

        private val gsonConveter: Gson = GsonBuilder().setLenient().create()

        val client: Retrofit
            get() {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gsonConveter))
                        .client(okHttpClient)
                        .build()
                }
                return retrofit!!
            }

    }
}