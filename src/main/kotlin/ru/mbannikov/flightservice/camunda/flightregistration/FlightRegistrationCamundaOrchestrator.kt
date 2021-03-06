package ru.mbannikov.flightservice.camunda.flightregistration

import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.mbannikov.flightservice.camunda.CamundaUtils.toValue
import ru.mbannikov.flightservice.camunda.client.CamundaClient
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.GOT_AUTHORIZE_DECISION_MESSAGE
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.IS_AUTHORIZED_CONTEXT_VARIABLE
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.NEED_AUTHORIZATION_CONTEXT_VARIABLE
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.ORDER_CONFIRMED_MESSAGE
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.ORDER_ID_CONTEXT_VARIABLE
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.PROCESS_KEY
import ru.mbannikov.flightservice.domain.order.api.OrderAuthorizedEvent
import ru.mbannikov.flightservice.domain.order.api.OrderConfirmedEvent
import ru.mbannikov.flightservice.domain.order.api.OrderCreatedEvent
import ru.mbannikov.flightservice.domain.order.api.OrderNotAuthorizedEvent
import ru.mbannikov.mescofe.eventhandling.annotation.EventHandler

@Component
class FlightRegistrationCamundaOrchestrator(
    camundaRestTemplate: RestTemplate
) {
    private val camundaClient = CamundaClient(baseUrl = "http://localhost:8080", restTemplate = camundaRestTemplate)

    @EventHandler
    fun on(e: OrderCreatedEvent) {
        val needAuthorization = true // TODO: get from company settings
        startProcess(orderId = e.orderId, needAuthorization = needAuthorization)
    }

    @EventHandler
    fun on(e: OrderConfirmedEvent) {
        sendMessageOrderConfirmed(orderId = e.orderId)
    }

    @EventHandler
    fun on(e: OrderAuthorizedEvent) {
        sendMessageGotAuthorizeDecision(e.orderId, isAuthorized = true)
    }

    @EventHandler
    fun on(e: OrderNotAuthorizedEvent) {
        sendMessageGotAuthorizeDecision(e.orderId, isAuthorized = false)
    }

    private fun startProcess(orderId: String, needAuthorization: Boolean) {
        camundaClient.startProcess(
            processKey = PROCESS_KEY,
            businessKey = orderId.toBusinessKey(),
            variables = mapOf(
                ORDER_ID_CONTEXT_VARIABLE to orderId.toValue(),
                NEED_AUTHORIZATION_CONTEXT_VARIABLE to needAuthorization.toValue()
            )
        )
    }

    private fun sendMessageOrderConfirmed(orderId: String) {
        camundaClient.sendMessage(
            messageName = ORDER_CONFIRMED_MESSAGE,
            businessKey = orderId.toBusinessKey(),
            variables = emptyMap()
        )
    }

    private fun sendMessageGotAuthorizeDecision(orderId: String, isAuthorized: Boolean) {
        camundaClient.sendMessage(
            messageName = GOT_AUTHORIZE_DECISION_MESSAGE,
            businessKey = orderId.toBusinessKey(),
            variables = mapOf(IS_AUTHORIZED_CONTEXT_VARIABLE to isAuthorized.toValue())
        )
    }

    private fun String.toBusinessKey() = "${PROCESS_KEY}_$this"
}
