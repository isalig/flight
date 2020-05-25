package io.aiico.flight.data

import io.aiico.flight.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {

    val api: SuggestionsApi = buildRetrofit()
        .create(SuggestionsApi::class.java)

    private fun buildOkHttpClient(): OkHttpClient =
        with(OkHttpClient.Builder()) {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().setLevel(Level.BODY))
            }
            build()
        }

    private fun buildRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(buildOkHttpClient())
            .baseUrl(BuildConfig.API_BASE_URL)
            .build()
}
