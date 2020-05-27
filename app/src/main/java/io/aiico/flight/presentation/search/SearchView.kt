package io.aiico.flight.presentation.search

import io.aiico.flight.domain.model.Destination
import io.aiico.flight.presentation.base.BaseView

interface SearchView : BaseView {

    fun showList(destinations: List<Destination>)

    fun showError(message: String?)
}
