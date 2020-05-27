package io.aiico.flight.presentation.search.list

import androidx.recyclerview.widget.DiffUtil
import io.aiico.flight.domain.model.Suggestion

class SuggestionDiffCallback : DiffUtil.ItemCallback<Suggestion>() {

    override fun areItemsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean =
        oldItem.location == newItem.location

    override fun areContentsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean =
        oldItem.fullName == newItem.fullName
}
