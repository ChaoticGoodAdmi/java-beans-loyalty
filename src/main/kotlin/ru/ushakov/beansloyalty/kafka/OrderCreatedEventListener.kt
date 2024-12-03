package ru.ushakov.beansloyalty.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.ushakov.beansloyalty.domain.LoyaltyEvent
import ru.ushakov.beansloyalty.domain.LoyaltyEventType
import ru.ushakov.beansloyalty.service.LoyaltyEventStoreService
import ru.ushakov.beansloyalty.service.LoyaltySnapshotService

@Service
class OrderCreatedEventListener(
    private val eventStoreService: LoyaltyEventStoreService,
    private val kafkaTemplate: KafkaTemplate<String, LoyaltyUpdatedEvent>,
    private val objectMapper: ObjectMapper,
    @Value("\${loyalty.bonus-percentage:0.5}") private val bonusPercentage: Double
) {

    @KafkaListener(topics = ["OrderCreated"], groupId = "beans-loyalty-service-group")
    fun handleOrderCreatedEvent(message: String) {
        println("OrderCreatedEvent received with payload: $message")
        val event = parseOrderCreatedEvent(message)
        val userId = event.userId
        val totalCost = event.totalCost
        val bonusPointsUsed = event.bonusPointsUsed

        val bonusPointsEarned = (totalCost * bonusPercentage).toInt()

        if (bonusPointsUsed > 0) {
            val pointsUsedEvent = LoyaltyEvent(
                id = generateUniqueId(),
                userId = userId,
                type = LoyaltyEventType.POINTS_USED,
                data = mapOf("points" to bonusPointsUsed)
            )
            eventStoreService.appendEvent(pointsUsedEvent)
            kafkaTemplate.send("LoyaltyUpdated", LoyaltyUpdatedEvent(userId, bonusPointsUsed, LoyaltyEventType.POINTS_USED))
        }

        if (bonusPointsEarned > 0) {
            val pointsAddedEvent = LoyaltyEvent(
                id = generateUniqueId(),
                userId = userId,
                type = LoyaltyEventType.POINTS_ADDED,
                data = mapOf("points" to bonusPointsEarned)
            )
            eventStoreService.appendEvent(pointsAddedEvent)
            kafkaTemplate.send("LoyaltyUpdated", LoyaltyUpdatedEvent(userId, bonusPointsUsed, LoyaltyEventType.POINTS_ADDED))
        }
    }

    private fun parseOrderCreatedEvent(message: String): OrderCreatedEvent {
        return objectMapper.readValue(message, OrderCreatedEvent::class.java)
    }

    private fun generateUniqueId(): String {
        return java.util.UUID.randomUUID().toString()
    }
}