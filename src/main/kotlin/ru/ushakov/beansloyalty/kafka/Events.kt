package ru.ushakov.beansloyalty.kafka

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ru.ushakov.beansloyalty.domain.LoyaltyEventType

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderCreatedEvent(
    val userId: String,
    val totalCost: Double,
    val bonusPointsUsed: Int
)

data class LoyaltyUpdatedEvent(
    val userId: String,
    val pointsAmount: Int,
    val loyaltyEventType: LoyaltyEventType
)