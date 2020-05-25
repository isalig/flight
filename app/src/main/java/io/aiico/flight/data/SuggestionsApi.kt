package io.aiico.flight.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SuggestionsApi {

    @GET("autocomplete")
    fun getSuggestions(
        @Query("term") searchQuery: String,
        @Query("lang") language: String = "ru"
    ): Single<SuggestionsResponse>
}