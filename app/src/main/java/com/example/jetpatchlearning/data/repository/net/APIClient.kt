package com.example.jetpatchlearning.data.repository.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIClient {
    private object Holder {
        val INSTANCE = APIClient()
    }

    // 派生
    companion object {
        val instance = Holder.INSTANCE
    }

    fun <T> instanceRetrofit(apiInterface: Class<T>): T {
        val mOkHttpClient = OkHttpClient().newBuilder().apply {
            readTimeout(10000, TimeUnit.SECONDS)
            connectTimeout(10000, TimeUnit.SECONDS)
            writeTimeout(10000, TimeUnit.SECONDS)
        }.build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .client(mOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(apiInterface)
    }


}