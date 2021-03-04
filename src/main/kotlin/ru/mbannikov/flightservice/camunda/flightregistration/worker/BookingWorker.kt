package ru.mbannikov.flightservice.camunda.flightregistration.worker

import org.camunda.bpm.client.task.ExternalTask
import org.camunda.bpm.client.task.ExternalTaskService
import org.springframework.stereotype.Component
import ru.mbannikov.flightservice.camunda.AbstractCamundaWorker
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.BOOKING_TOPIC
import ru.mbannikov.flightservice.domain.order.api.BookOrderCommand
import ru.mbannikov.flightservice.utils.Logging
import ru.mbannikov.mescofe.cqrs.CommandGateway
import java.util.Arrays

@Component
class BookingWorker(
    private val commandGateway: CommandGateway
) : AbstractCamundaWorker(BOOKING_TOPIC), Logging {

    override fun doWork(externalTask: ExternalTask, externalTaskService: ExternalTaskService) {
        val orderId: String = externalTask.getVariable(CamundaConstants.ORDER_ID_CONTEXT_VARIABLE)
        val command = BookOrderCommand(orderId)

        log.info { "Execute camunda BookingWorker for orderId=$orderId" }

        try {
            commandGateway.send(command)
            externalTaskService.complete(externalTask)
        } catch (e: Throwable) {
            externalTaskService.handleFailure(externalTask, "Something went wrong", Arrays.toString(e.stackTrace), 0, 0)
        }
    }
}
