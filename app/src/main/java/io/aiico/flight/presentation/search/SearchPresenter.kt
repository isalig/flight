package io.aiico.flight.presentation.search

import android.os.Bundle
import android.util.Log
import io.aiico.flight.BuildConfig
import io.aiico.flight.addTo
import io.aiico.flight.domain.model.Suggestion
import io.aiico.flight.domain.interactor.SuggestionsInteractor
import io.aiico.flight.presentation.base.BasePresenter
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchPresenter(
    private val suggestionsInteractor: SuggestionsInteractor,
    view: SearchView
) : BasePresenter<SearchView>(view) {

    private val queryPublisher = PublishSubject.create<String>()
    private var suggestions: List<Suggestion> = emptyList()

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState
            ?.getParcelableArrayList<Suggestion>(KEY_SUGGESTIONS)
            ?.let { suggestions -> this.suggestions = suggestions }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(KEY_SUGGESTIONS, ArrayList(suggestions))
    }

    override fun onViewCreated(configurationChanged: Boolean) {
        queryPublisher
            .debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMapSingle { query ->
                suggestionsInteractor
                    .getSuggestions(query)
                    .doOnError(::onError)
                    .onErrorReturn { suggestions }
            }
            .distinctUntilChanged()
            .subscribe(::onSuggestionsUpdate, ::onError)
            .addTo(compositeDisposable)

        if (!configurationChanged) {
            suggestionsInteractor
                .getSuggestions("")
                .subscribe(::onSuggestionsUpdate, ::onError)
                .addTo(compositeDisposable)
        } else {
            view.showList(suggestions)
        }
    }

    private fun onSuggestionsUpdate(suggestions: List<Suggestion>) {
        this.suggestions = suggestions
        view.showList(suggestions)
    }

    private fun onError(error: Throwable) {
        view.showError(error.message)
        if (BuildConfig.DEBUG) {
            Log.e("SearchPresenter", error.message, error)
        }
    }

    fun onQueryChanged(query: String?) {
        queryPublisher.onNext(query ?: "")
    }

    companion object {
        private const val KEY_SUGGESTIONS = "key_suggestions"
    }
}
