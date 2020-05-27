package io.aiico.flight.presentation.search.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.aiico.flight.R
import io.aiico.flight.domain.model.Destination

class DestinationsAdapter :
    ListAdapter<Destination, DestinationViewHolder>(DestinationDiffCallback()
) {

    var itemClickCallback: ((Destination) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_search_suggestion, parent, false)

        return DestinationViewHolder(itemView).apply {
            itemView.setOnClickListener {
                itemClickCallback?.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
