package io.aiico.flight

import android.app.Application
import android.util.Log
import io.reactivex.plugins.RxJavaPlugins

@Suppress("unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) {
            // throw possible errors during development
            RxJavaPlugins.setErrorHandler { error ->
                Log.e("RxErrorHandler", error.message, error)
            }
        }
    }
}
