package io.aiico.flight

import io.aiico.flight.data.DefaultSuggestionsService
import io.aiico.flight.data.NetworkService
import io.aiico.flight.data.SuggestionsRepository
import io.aiico.flight.domain.interactor.FlightInteractor
import io.aiico.flight.domain.interactor.SuggestionsInteractor

object ServiceLocator {

    fun getSuggestionsInteractor(): SuggestionsInteractor =
        SuggestionsInteractor(
            createSuggestionsRepository()
        )

    private fun createSuggestionsRepository(): SuggestionsRepository =
        SuggestionsRepository(NetworkService.api, DefaultSuggestionsService.suggestions)

    fun getFlightInteractor(): FlightInteractor = FlightInteractor()
}
