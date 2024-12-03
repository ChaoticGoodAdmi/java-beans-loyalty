package ru.ushakov.beansloyalty.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import ru.ushakov.beansloyalty.domain.LoyaltyEvent
import ru.ushakov.beansloyalty.domain.LoyaltyEventType
import ru.ushakov.beansloyalty.domain.LoyaltySnapshot

@Service
class LoyaltySnapshotService(
    private val redisTemplate: RedisTemplate<String, LoyaltySnapshot>
) {

    fun updateSnapshot(event: LoyaltyEvent) {
        val userId = event.userId
        val snapshot = getSnapshot(userId) ?: LoyaltySnapshot(userId)

        when (event.type) {
            LoyaltyEventType.POINTS_ADDED -> {
                val points = (event.data["points"] as Number).toInt()
                snapshot.currentPoints += points
            }
            LoyaltyEventType.POINTS_USED -> {
                val points = (event.data["points"] as Number).toInt()
                snapshot.currentPoints -= points
            }
        }

        redisTemplate.opsForValue()[userId] = snapshot
    }

    fun getSnapshot(userId: String): LoyaltySnapshot? {
        return redisTemplate.opsForValue()[userId]
    }
}