package io.aiico.flight.presentation.search.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.aiico.flight.domain.Suggestion
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_suggestion.*

class SuggestionViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(suggestion: Suggestion) {
        cityNameTextView.text = suggestion.fullName
    }
}
