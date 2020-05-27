package io.aiico.flight.domain.model

import com.google.gson.annotations.SerializedName

data class Suggestion(
    @SerializedName("fullname") val fullName: String,
    @SerializedName("location") val location: Location,
    @SerializedName("city") val city: String,
    @SerializedName("iata") val iata: List<String> = emptyList()
)
