package io.aiico.flight.presentation.route

import android.os.Bundle
import io.aiico.flight.domain.model.Destination
import io.aiico.flight.presentation.base.BasePresenter

class RoutePresenter(view: RouteView) : BasePresenter<RouteView>(view) {

    private var departureDestination: Destination? = null
    private var arrivalDestination: Destination? = null

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        departureDestination = savedInstanceState?.getParcelable(KEY_DEPARTURE_DESTINATION)
        arrivalDestination = savedInstanceState?.getParcelable(KEY_ARRIVAL_DESTINATION)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(outState) {
            putParcelable(KEY_DEPARTURE_DESTINATION, departureDestination)
            putParcelable(KEY_ARRIVAL_DESTINATION, arrivalDestination)
        }
    }

    override fun onViewCreated(configurationChanged: Boolean) {
        updateSearchButtonState()
    }

    fun onDestinationSelected(destination: Destination, tag: String) {
        when (tag) {
            TAG_DEPARTURE_POINT_SEARCH -> {
                departureDestination = destination
                view.showDeparturePointName(destination.fullName)
            }

            TAG_ARRIVAL_POINT_SEARCH -> {
                arrivalDestination = destination
                view.showArrivalPointName(destination.fullName)
            }
        }
        updateSearchButtonState()
    }

    private fun updateSearchButtonState() {
        view.setSearchButtonEnabled(
            departureDestination != null &&
                    arrivalDestination != null &&
                    departureDestination != arrivalDestination
        )
    }

    fun onDeparturePointClick() {
        view.showCitySearchScreen(TAG_DEPARTURE_POINT_SEARCH)
    }

    fun onArrivalPointClick() {
        view.showCitySearchScreen(TAG_ARRIVAL_POINT_SEARCH)
    }

    fun onSearchClick() {
        view.showFlight(departureDestination!!, arrivalDestination!!)
    }

    companion object {

        private const val KEY_DEPARTURE_DESTINATION = "key_departure_destination"
        private const val KEY_ARRIVAL_DESTINATION = "key_arrival_destination"

        private const val TAG_DEPARTURE_POINT_SEARCH = "departure_point_search"
        private const val TAG_ARRIVAL_POINT_SEARCH = "arrival_point_search"
    }
}
