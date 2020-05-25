package io.aiico.flight.domain

import io.aiico.flight.data.SuggestionsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SuggestionsInteractor(private val suggestionsRepository: SuggestionsRepository) {

    fun getSuggestions(rawQuery: String): Single<List<Suggestion>> =
        suggestionsRepository
            .getSuggestions(rawQuery.trim())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
