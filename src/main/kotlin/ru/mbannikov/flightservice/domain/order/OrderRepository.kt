package ru.mbannikov.flightservice.domain.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import ru.mbannikov.flightservice.domain.order.model.Order
import java.lang.IllegalStateException

interface OrderRepository : JpaRepository<Order, String> {
    @JvmDefault
    fun findByIdOrThrow(orderId: String): Order = findByIdOrNull(orderId) ?: throw IllegalStateException("Order with id=$orderId not found!")
}