package ru.mbannikov.flightservice.domain.order.model

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class FlightInfo(
    @Column(name = "flight_info_id")
    val id: String,

    @Column(name = "flight_info_source_region")
    val sourceRegion: String,

    @Column(name = "flight_info_destination_region")
    val destinationRegion: String,

    @Column(name = "flight_info_price_when_chosen")
    val priceWhenChosen: BigDecimal
)