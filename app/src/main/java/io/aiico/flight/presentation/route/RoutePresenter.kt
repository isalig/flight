package io.aiico.flight.presentation.route

import android.os.Bundle
import io.aiico.flight.domain.model.Suggestion
import io.aiico.flight.presentation.base.BasePresenter

class RoutePresenter(view: RouteView) : BasePresenter<RouteView>(view) {

    private var departurePointSuggestion: Suggestion? = null
    private var arrivalPointSuggestion: Suggestion? = null

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        departurePointSuggestion = savedInstanceState?.getParcelable(KEY_DEPARTURE_SUGGESTION)
        arrivalPointSuggestion = savedInstanceState?.getParcelable(KEY_ARRIVAL_SUGGESTION)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(outState) {
            putParcelable(KEY_DEPARTURE_SUGGESTION, departurePointSuggestion)
            putParcelable(KEY_ARRIVAL_SUGGESTION, arrivalPointSuggestion)
        }
    }

    override fun onViewCreated(configurationChanged: Boolean) {
        updateSearchButtonState()
    }

    fun onSuggestionSelected(suggestion: Suggestion, tag: String) {
        when (tag) {
            TAG_DEPARTURE_POINT_SEARCH -> {
                departurePointSuggestion = suggestion
                view.showDeparturePointName(suggestion.fullName)
            }

            TAG_ARRIVAL_POINT_SEARCH -> {
                arrivalPointSuggestion = suggestion
                view.showArrivalPointName(suggestion.fullName)
            }
        }
        updateSearchButtonState()
    }

    private fun updateSearchButtonState() {
        view.setSearchButtonEnabled(
            departurePointSuggestion != null &&
                    arrivalPointSuggestion != null &&
                    departurePointSuggestion != arrivalPointSuggestion
        )
    }

    fun onDeparturePointClick() {
        view.showCitySearchScreen(TAG_DEPARTURE_POINT_SEARCH)
    }

    fun onArrivalPointClick() {
        view.showCitySearchScreen(TAG_ARRIVAL_POINT_SEARCH)
    }

    fun onSearchClick() {
        view.showFlight(departurePointSuggestion!!, arrivalPointSuggestion!!)
    }

    companion object {

        private const val KEY_DEPARTURE_SUGGESTION = "key_departure_suggestion"
        private const val KEY_ARRIVAL_SUGGESTION = "key_arrival_suggestion"

        private const val TAG_DEPARTURE_POINT_SEARCH = "departure_point_search"
        private const val TAG_ARRIVAL_POINT_SEARCH = "arrival_point_search"
    }
}
