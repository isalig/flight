package io.aiico.flight.domain

import io.aiico.flight.data.SuggestionsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SuggestionsInteractor(private val suggestionsRepository: SuggestionsRepository) {

    fun getSuggestions(rawQuery: String): Single<List<Suggestion>> =
        Single.fromCallable { rawQuery.trim() }
            .flatMap { query -> suggestionsRepository.getSuggestions(query) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}