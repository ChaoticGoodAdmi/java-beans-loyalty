package ru.ushakov.beansloyalty

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.ushakov.beansloyalty.domain.LoyaltyEvent
import ru.ushakov.beansloyalty.service.LoyaltyEventStoreService
import ru.ushakov.beansloyalty.service.LoyaltySnapshotService

@RestController
class LoyaltyController(
    private val snapshotService: LoyaltySnapshotService,
    private val loyaltyEventStoreService: LoyaltyEventStoreService
) {

    @GetMapping("/loyalty/{userId}/balance")
    fun getBalance(@PathVariable userId: String): ResponseEntity<LoyaltyBalanceResponse> {
        val snapshot = snapshotService.getSnapshot(userId)

        return if (snapshot != null) {
            ResponseEntity.ok(
                LoyaltyBalanceResponse(
                    userId = snapshot.userId,
                    currentPoints = snapshot.currentPoints,
                    lastUpdated = snapshot.lastUpdated.toString()
                )
            )
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/loyalty/{userId}/history")
    fun getHistory(@PathVariable userId: String): ResponseEntity<List<LoyaltyEvent>> {
        val userEvents = loyaltyEventStoreService.getUserEvents(userId).sortedBy { it.timestamp }.reversed()

        return if (userEvents.isNotEmpty()) {
            ResponseEntity.ok(userEvents)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

data class LoyaltyBalanceResponse(
    val userId: String,
    val currentPoints: Int,
    val lastUpdated: String
)