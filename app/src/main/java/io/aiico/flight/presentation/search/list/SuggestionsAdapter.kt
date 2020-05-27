package io.aiico.flight.presentation.search.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.aiico.flight.R
import io.aiico.flight.domain.model.Suggestion

class SuggestionsAdapter :
    ListAdapter<Suggestion, SuggestionViewHolder>(SuggestionDiffCallback()
) {

    var itemClickCallback: ((Suggestion) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_suggestion, parent, false)

        return SuggestionViewHolder(itemView).apply {
            itemView.setOnClickListener {
                itemClickCallback?.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
