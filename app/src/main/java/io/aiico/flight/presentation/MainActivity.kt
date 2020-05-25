package io.aiico.flight.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.aiico.flight.presentation.route.RouteFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState ?: addInitialFragment()
    }

    private fun addInitialFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(
                android.R.id.content,
                RouteFragment.newInstance()
            )
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .commit()
    }
}
