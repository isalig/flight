package io.aiico.flight.presentation.search

import android.os.Bundle
import android.util.Log
import io.aiico.flight.BuildConfig
import io.aiico.flight.addTo
import io.aiico.flight.domain.interactor.DestinationsInteractor
import io.aiico.flight.domain.model.Destination
import io.aiico.flight.presentation.base.BasePresenter
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchPresenter(
    private val destinationsInteractor: DestinationsInteractor,
    view: SearchView
) : BasePresenter<SearchView>(view) {

    private val queryPublisher = PublishSubject.create<String>()
    private var destinations: List<Destination> = emptyList()

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState
            ?.getParcelableArrayList<Destination>(KEY_DESTINATIONS)
            ?.let { destinations -> this.destinations = destinations }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(KEY_DESTINATIONS, ArrayList(destinations))
    }

    override fun onViewCreated(configurationChanged: Boolean) {
        queryPublisher
            .debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMapSingle { query ->
                destinationsInteractor
                    .getDestinations(query)
                    .doOnError(::onError)
                    .onErrorReturn { destinations }
            }
            .distinctUntilChanged()
            .subscribe(::onDestinationsUpdate, ::onError)
            .addTo(compositeDisposable)

        if (!configurationChanged) {
            destinationsInteractor
                .getDestinations("")
                .subscribe(::onDestinationsUpdate, ::onError)
                .addTo(compositeDisposable)
        } else {
            view.showList(destinations)
        }
    }

    private fun onDestinationsUpdate(destinations: List<Destination>) {
        this.destinations = destinations
        view.showList(destinations)
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
        private const val KEY_DESTINATIONS = "key_destinations"
    }
}
