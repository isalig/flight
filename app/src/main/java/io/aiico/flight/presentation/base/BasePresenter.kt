package io.aiico.flight.presentation.base

import android.os.Bundle
import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V : BaseView>(protected val view: V) {

    protected val compositeDisposable = CompositeDisposable()

    open fun onViewCreated(configurationChanged: Boolean) {
        // do nothing
    }

    @CallSuper
    open fun onDestroy() {
        compositeDisposable.clear()
    }

    open fun onSaveInstanceState(outState: Bundle) {
        // do nothing
    }

    open fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        // do nothing
    }
}
