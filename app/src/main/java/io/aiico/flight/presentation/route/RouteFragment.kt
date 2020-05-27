package io.aiico.flight.presentation.route

import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import io.aiico.flight.R
import io.aiico.flight.domain.model.Destination
import io.aiico.flight.presentation.base.BaseFragment
import io.aiico.flight.presentation.flight.FlightFragment
import io.aiico.flight.presentation.search.SearchDialog
import kotlinx.android.synthetic.main.fragment_route_points.*

class RouteFragment : BaseFragment<RoutePresenter>(), SearchDialog.DestinationSelectionListener,
    RouteView {

    override fun createPresenter(): RoutePresenter = RoutePresenter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_route_points, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orientation = resources.configuration.orientation
        pointsImageView.isVisible = orientation != Configuration.ORIENTATION_LANDSCAPE
        initRoutePointEditText(departureEditText)
        initRoutePointEditText(arrivalEditText)
        departureEditText.setOnClickListener {
            presenter.onDeparturePointClick()
        }
        arrivalEditText.setOnClickListener {
            presenter.onArrivalPointClick()
        }
        searchButton.setOnClickListener {
            presenter.onSearchClick()
        }
    }

    private fun initRoutePointEditText(editText: EditText) {
        editText.inputType = InputType.TYPE_NULL
        editText.isCursorVisible = false
        editText.isFocusable = false
        editText.isFocusableInTouchMode = false
    }

    override fun showFlight(
        departureDestination: Destination,
        arrivalDestination: Destination
    ) {
        requireFragmentManager()
            .beginTransaction()
            .replace(
                android.R.id.content,
                FlightFragment.newInstance(departureDestination, arrivalDestination)
            )
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .addToBackStack(null)
            .commit()
    }

    override fun showDeparturePointName(name: String) {
        departureEditText.setText(name)
    }

    override fun showArrivalPointName(name: String) {
        arrivalEditText.setText(name)
    }

    override fun onDestinationSelected(destination: Destination, tag: String) {
        presenter.onDestinationSelected(destination, tag)
    }

    override fun setSearchButtonEnabled(enabled: Boolean) {
        searchButton.isEnabled = enabled
    }

    override fun showCitySearchScreen(tag: String) {
        SearchDialog
            .newInstance()
            .show(childFragmentManager, tag)
    }

    companion object {

        fun newInstance() = RouteFragment()
    }
}
