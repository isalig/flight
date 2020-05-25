package io.aiico.flight.data

import com.google.gson.annotations.SerializedName
import io.aiico.flight.domain.Suggestion

data class SuggestionsResponse(@SerializedName("cities") val suggestions: List<Suggestion> = emptyList())
