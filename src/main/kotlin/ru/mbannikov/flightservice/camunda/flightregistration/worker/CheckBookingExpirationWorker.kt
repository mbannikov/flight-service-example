package ru.mbannikov.flightservice.camunda.flightregistration.worker

import org.camunda.bpm.client.task.ExternalTask
import org.camunda.bpm.client.task.ExternalTaskService
import org.springframework.stereotype.Component
import ru.mbannikov.flightservice.camunda.AbstractCamundaWorker
import ru.mbannikov.flightservice.camunda.client.dto.CamundaValue
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.CHECK_BOOKING_EXPIRATION_TOPIC
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.IS_BOOKING_EXPIRED_VARIABLE
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.REEBOOKING_TOPIC
import ru.mbannikov.flightservice.utils.Logging

@Component
class CheckBookingExpirationWorker : AbstractCamundaWorker(CHECK_BOOKING_EXPIRATION_TOPIC), Logging {

    override fun doWork(externalTask: ExternalTask, externalTaskService: ExternalTaskService) {
        val orderId: String = externalTask.getVariable(CamundaConstants.ORDER_ID_CONTEXT_VARIABLE)

        log.info { "Execute camunda CheckBookingExpirationWorker for orderId=$orderId" }

        val payload = mapOf<String, Any>(IS_BOOKING_EXPIRED_VARIABLE to true)
        externalTaskService.complete(externalTask, payload)
    }
}
