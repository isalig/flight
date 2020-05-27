package io.aiico.flight.presentation.search.list

import androidx.recyclerview.widget.DiffUtil
import io.aiico.flight.domain.model.Destination

class DestinationDiffCallback : DiffUtil.ItemCallback<Destination>() {

    override fun areItemsTheSame(oldItem: Destination, newItem: Destination): Boolean =
        oldItem.iata == newItem.iata

    override fun areContentsTheSame(oldItem: Destination, newItem: Destination): Boolean =
        oldItem.fullName == newItem.fullName
}
