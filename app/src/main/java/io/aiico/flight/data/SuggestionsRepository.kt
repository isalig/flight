package io.aiico.flight.data

import io.aiico.flight.domain.model.Suggestion
import io.reactivex.Single

class SuggestionsRepository(
    private val api: SuggestionsApi,
    private val defaultSuggestions: List<Suggestion>
) {

    fun getSuggestions(query: String): Single<List<Suggestion>> =
        if (query.isBlank()) {
            Single.just(defaultSuggestions)
        } else {
            api
                .getSuggestions(query)
                .map { suggestionsResponse -> suggestionsResponse.suggestions }
        }
}
