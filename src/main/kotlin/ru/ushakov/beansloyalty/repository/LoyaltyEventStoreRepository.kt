package ru.ushakov.beansloyalty.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.ushakov.beansloyalty.domain.LoyaltyEvent

@Repository
interface EventStoreRepository : MongoRepository<LoyaltyEvent, String> {
    fun findAllByUserIdOrderByTimestampAsc(userId: String): List<LoyaltyEvent>
}