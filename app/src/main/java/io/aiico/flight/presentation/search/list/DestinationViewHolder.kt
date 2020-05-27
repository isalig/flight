package io.aiico.flight.presentation.search.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.aiico.flight.domain.model.Destination
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_search_suggestion.*

class DestinationViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(destination: Destination) {
        cityNameTextView.text = destination.fullName
        iataTextView.text = destination.iata
    }
}
