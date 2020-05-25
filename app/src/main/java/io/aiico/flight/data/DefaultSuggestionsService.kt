package io.aiico.flight.data

import io.aiico.flight.domain.Location
import io.aiico.flight.domain.Suggestion

object DefaultSuggestionsService {

    val suggestions = arrayListOf(
        Suggestion(
            fullName = "Москва, Россия",
            city = "Москва",
            location = Location(
                latitude = 55.75204086303711F,
                longitude = 37.61750793457031F
            )
        ),
        Suggestion(
            fullName = "Рим, Италия",
            city = "Рим",
            location = Location(
                latitude = 41.8902F, longitude = 12.492214F
            )
        ),
        Suggestion(
            fullName = "Санкт-Петербург, Россия",
            city = "Санкт-Петербург",
            location = Location(latitude = 59.95F, longitude = 30.316668F)
        ),
        Suggestion(
            fullName = "Нью-Йорк, Нью-Йорк, США",
            city = "Нью-Йорк",
            location = Location(latitude = 40.75603F, longitude = -73.98695F)
        )
    )
}
