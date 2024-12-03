package ru.ushakov.beansloyalty.domain

import java.time.Instant

data class LoyaltyEvent(
    val id: String,
    val userId: String,
    val type: LoyaltyEventType,
    val data: Map<String, Any>,
    val timestamp: Instant = Instant.now()
)

enum class LoyaltyEventType {
    POINTS_ADDED,
    POINTS_USED
}