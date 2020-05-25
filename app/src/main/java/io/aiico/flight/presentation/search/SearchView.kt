package io.aiico.flight.presentation.search

import io.aiico.flight.presentation.base.BaseView
import io.aiico.flight.domain.Suggestion

interface SearchView : BaseView {

    fun showList(suggestions: List<Suggestion>)

    fun showError(message: String?)
}
