package ru.mbannikov.flightservice.domain.order

import org.axonframework.eventsourcing.AggregateFactory
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.modelling.command.Repository
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

//@Configuration
//class FlightOrderConfiguration(
//    private val eventStore: EventStore
//) {
//    @Bean
//    fun flightOrderAggregateFactory(): AggregateFactory<FlightOrder> =
//        SpringPrototypeAggregateFactory<FlightOrder>("flightOrder")
//
//
//    @Bean("customFlightOrderRepository")
//    fun flightOrderRepository(factory: AggregateFactory<FlightOrder>): Repository<FlightOrder> =
//        EventSourcingRepository.builder(FlightOrder::class.java)
//            .aggregateFactory(factory)
//            .eventStore(eventStore)
//            .build<EventSourcingRepository<FlightOrder>>()
//}