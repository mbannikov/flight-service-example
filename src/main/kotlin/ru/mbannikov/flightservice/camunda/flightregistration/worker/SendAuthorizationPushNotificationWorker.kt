package ru.mbannikov.flightservice.camunda.flightregistration.worker

import org.camunda.bpm.client.task.ExternalTask
import org.camunda.bpm.client.task.ExternalTaskService
import org.springframework.stereotype.Component
import ru.mbannikov.flightservice.camunda.AbstractCamundaWorker
import ru.mbannikov.flightservice.camunda.client.dto.CamundaValue
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.SEND_AUTHORIZATION_PUSH_NOTIFICATION_TOPIC
import ru.mbannikov.flightservice.utils.Logging

@Component
class SendAuthorizationPushNotificationWorker : AbstractCamundaWorker(SEND_AUTHORIZATION_PUSH_NOTIFICATION_TOPIC), Logging {

    override fun doWork(externalTask: ExternalTask, externalTaskService: ExternalTaskService) {
        val orderId: String = externalTask.getVariable(CamundaConstants.ORDER_ID_CONTEXT_VARIABLE)

        log.info { "Execute camunda SendAuthorizationPushNotificationWorker for orderId=$orderId" }

        externalTaskService.complete(externalTask)
    }
}
