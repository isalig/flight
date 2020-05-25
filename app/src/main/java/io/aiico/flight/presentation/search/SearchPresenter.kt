package io.aiico.flight.presentation.search

import android.os.Bundle
import android.util.Log
import io.aiico.flight.BuildConfig
import io.aiico.flight.presentation.base.BasePresenter
import io.aiico.flight.domain.Suggestion
import io.aiico.flight.domain.SuggestionsInteractor
import io.aiico.flight.addTo
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchPresenter(
    private val suggestionsInteractor: SuggestionsInteractor,
    view: SearchView
) : BasePresenter<SearchView>(view) {

    private val queryPublisher = PublishSubject.create<String>()
    private var suggestions: List<Suggestion> = emptyList()

    fun onQueryChanged(query: String?) {
        queryPublisher.onNext(query ?: "")
    }

    override fun onViewCreated(configurationChanged: Boolean) {
        queryPublisher
            .debounce(200, TimeUnit.MILLISECONDS)
            .switchMapSingle { query -> suggestionsInteractor.getSuggestions(query) }
            .subscribe(::onSuggestionsUpdate, ::onError)
            .addTo(compositeDisposable)

        if (!configurationChanged) {
            suggestionsInteractor
                .getSuggestions("")
                .subscribe(::onSuggestionsUpdate, ::onError)
                .addTo(compositeDisposable)
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState
            ?.getParcelableArrayList<Suggestion>(KEY_SUGGESTIONS)
            ?.let { suggestions -> view.showList(suggestions) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(KEY_SUGGESTIONS, ArrayList(suggestions))
    }

    companion object {
        private const val KEY_SUGGESTIONS = "key_suggestions"
    }
}
