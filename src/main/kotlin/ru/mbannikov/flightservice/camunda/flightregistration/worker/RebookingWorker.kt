package ru.mbannikov.flightservice.camunda.flightregistration.worker

import org.camunda.bpm.client.task.ExternalTask
import org.camunda.bpm.client.task.ExternalTaskService
import org.springframework.stereotype.Component
import ru.mbannikov.flightservice.camunda.AbstractCamundaWorker
import ru.mbannikov.flightservice.camunda.client.dto.CamundaValue
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.REEBOOKING_TOPIC
import ru.mbannikov.flightservice.utils.Logging

@Component
class RebookingWorker : AbstractCamundaWorker(REEBOOKING_TOPIC), Logging {

    override fun doWork(externalTask: ExternalTask, externalTaskService: ExternalTaskService) {
        val orderId: String = externalTask.getVariable(CamundaConstants.ORDER_ID_CONTEXT_VARIABLE)

        log.info { "Execute camunda RebookingWorker for orderId=$orderId" }

        externalTaskService.complete(externalTask)
    }
}
