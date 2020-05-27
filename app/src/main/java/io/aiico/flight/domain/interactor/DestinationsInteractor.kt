package io.aiico.flight.domain.interactor

import io.aiico.flight.data.SuggestionsRepository
import io.aiico.flight.domain.model.Destination
import io.aiico.flight.domain.model.Suggestion
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DestinationsInteractor(private val suggestionsRepository: SuggestionsRepository) {

    fun getDestinations(rawQuery: String): Single<List<Destination>> =
        suggestionsRepository
            .getSuggestions(rawQuery.trim())
            .toObservable()
            .flatMapIterable { suggestions -> suggestions }
            .filter { suggestion -> suggestion.iata.isNotEmpty() }
            .flatMapIterable { suggestion -> suggestionToDestinations(suggestion) }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private fun suggestionToDestinations(suggestion: Suggestion): List<Destination> {
        return ArrayList<Destination>().apply {
            suggestion.iata.forEach { iata ->
                val destination = Destination(
                    suggestion.fullName,
                    suggestion.location,
                    suggestion.city, iata
                )
                add(destination)
            }
        }
    }
}
