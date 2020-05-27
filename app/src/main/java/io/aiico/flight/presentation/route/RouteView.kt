package io.aiico.flight.presentation.route

import io.aiico.flight.domain.model.Destination
import io.aiico.flight.presentation.base.BaseView

interface RouteView : BaseView {

    fun showFlight(departureDestination: Destination, arrivalDestination: Destination)

    fun showCitySearchScreen(tag: String)

    fun showDeparturePointName(name: String)

    fun showArrivalPointName(name: String)

    fun setSearchButtonEnabled(enabled: Boolean)
}
