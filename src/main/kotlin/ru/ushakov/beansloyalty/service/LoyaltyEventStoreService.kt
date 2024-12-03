package ru.ushakov.beansloyalty.service

import org.springframework.stereotype.Service
import ru.ushakov.beansloyalty.domain.LoyaltyEvent
import ru.ushakov.beansloyalty.repository.EventStoreRepository

@Service
class LoyaltyEventStoreService(
    private val repository: EventStoreRepository,
    private val snapshotService: LoyaltySnapshotService
) {

    fun appendEvent(event: LoyaltyEvent) {
        repository.save(event)
        snapshotService.updateSnapshot(event)
    }

    fun getUserEvents(userId: String): List<LoyaltyEvent> {
        return repository.findAllByUserIdOrderByTimestampAsc(userId)
    }
}