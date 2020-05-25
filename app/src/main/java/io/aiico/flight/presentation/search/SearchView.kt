package io.aiico.flight.presentation.search

import io.aiico.flight.domain.Suggestion
import io.aiico.flight.presentation.base.BaseView

interface SearchView : BaseView {

    fun showList(suggestions: List<Suggestion>)

    fun showError(message: String?)
}
