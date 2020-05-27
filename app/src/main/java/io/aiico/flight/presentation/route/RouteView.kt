package io.aiico.flight.presentation.route

import io.aiico.flight.domain.model.Suggestion
import io.aiico.flight.presentation.base.BaseView

interface RouteView : BaseView {

    fun showFlight(departurePointSuggestion: Suggestion, arrivalPointSuggestion: Suggestion)

    fun showCitySearchScreen(tag: String)

    fun showDeparturePointName(name: String)

    fun showArrivalPointName(name: String)

    fun setSearchButtonEnabled(enabled: Boolean)
}
