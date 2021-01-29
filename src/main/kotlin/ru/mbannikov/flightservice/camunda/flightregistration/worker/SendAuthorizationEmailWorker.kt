package ru.mbannikov.flightservice.camunda.flightregistration.worker

import org.camunda.bpm.client.task.ExternalTask
import org.camunda.bpm.client.task.ExternalTaskService
import org.springframework.stereotype.Component
import ru.mbannikov.flightservice.camunda.AbstractCamundaWorker
import ru.mbannikov.flightservice.camunda.client.dto.CamundaValue
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.SEND_AUTHORIZATION_EMAIL_TOPIC
import ru.mbannikov.flightservice.utils.Logging

@Component
class SendAuthorizationEmailWorker : AbstractCamundaWorker(SEND_AUTHORIZATION_EMAIL_TOPIC), Logging {

    override fun doWork(externalTask: ExternalTask, externalTaskService: ExternalTaskService) {
        val orderId: String = externalTask.getVariable(CamundaConstants.ORDER_ID_CONTEXT_VARIABLE)

        log.info { "Execute camunda SendAuthorizationEmailWorker for orderId=$orderId" }

        externalTaskService.complete(externalTask)
    }
}
