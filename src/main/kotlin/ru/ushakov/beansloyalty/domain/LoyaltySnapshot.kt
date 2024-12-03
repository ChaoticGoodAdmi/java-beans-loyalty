package ru.ushakov.beansloyalty.domain

import java.time.Instant

data class LoyaltySnapshot(
    var userId: String = "",
    var currentPoints: Int = 0,
    var lastUpdated: Instant = Instant.now()
)