package ru.ushakov.beansloyalty.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import ru.ushakov.beansloyalty.domain.LoyaltyEvent
import ru.ushakov.beansloyalty.service.LoyaltyEventStoreService
import ru.ushakov.beansloyalty.service.LoyaltySnapshotService
import java.time.LocalDateTime

@RestController
class LoyaltyController(
    private val snapshotService: LoyaltySnapshotService,
    private val loyaltyEventStoreService: LoyaltyEventStoreService
) {

    @GetMapping("/loyalty/balance")
    fun getBalance(@RequestHeader(name = "X-UserId", required = true) userId: String): ResponseEntity<LoyaltyBalanceResponse> {
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
            ResponseEntity.ok(
                LoyaltyBalanceResponse(
                    userId = userId,
                    currentPoints = 0,
                    lastUpdated = LocalDateTime.now().toString()
                )
            )
        }
    }

    @GetMapping("/loyalty/history")
    fun getHistory(@RequestHeader(name = "X-UserId", required = true) userId: String): ResponseEntity<List<LoyaltyEvent>> {
        val userEvents = loyaltyEventStoreService.getUserEvents(userId).sortedBy { it.timestamp }.reversed()

        return if (userEvents.isNotEmpty()) {
            ResponseEntity.ok(userEvents)
        } else {
            ResponseEntity.ok(listOf())
        }
    }
}

data class LoyaltyBalanceResponse(
    val userId: String,
    val currentPoints: Int,
    val lastUpdated: String
)