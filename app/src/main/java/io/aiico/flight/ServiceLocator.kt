package io.aiico.flight

import io.aiico.flight.data.DefaultSuggestionsService
import io.aiico.flight.data.NetworkService
import io.aiico.flight.data.SuggestionsRepository
import io.aiico.flight.domain.SuggestionsInteractor

object ServiceLocator {

    fun getSuggestionsInteractor(): SuggestionsInteractor =
        SuggestionsInteractor(createSuggestionsRepository())

    private fun createSuggestionsRepository(): SuggestionsRepository =
        SuggestionsRepository(NetworkService.api, DefaultSuggestionsService.suggestions)
}