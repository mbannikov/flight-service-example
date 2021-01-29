package ru.mbannikov.flightservice.camunda.flightregistration.worker

import org.axonframework.commandhandling.gateway.CommandGateway
import org.camunda.bpm.client.task.ExternalTask
import org.camunda.bpm.client.task.ExternalTaskService
import org.springframework.stereotype.Component
import ru.mbannikov.flightservice.camunda.AbstractCamundaWorker
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants
import ru.mbannikov.flightservice.camunda.flightregistration.CamundaConstants.BOOKING_TOPIC
import ru.mbannikov.flightservice.domain.order.api.BookFlightOrderCommand
import ru.mbannikov.flightservice.utils.Logging
import java.util.Arrays

@Component
class BookingWorker(
    private val commandGateway: CommandGateway
) : AbstractCamundaWorker(BOOKING_TOPIC), Logging {

    override fun doWork(externalTask: ExternalTask, externalTaskService: ExternalTaskService) {
        val orderId: String = externalTask.getVariable(CamundaConstants.ORDER_ID_CONTEXT_VARIABLE)
        val command = BookFlightOrderCommand(orderId)

        log.info { "Execute camunda BookingWorker for orderId=$orderId" }

        try {
            commandGateway.sendAndWait<Unit>(command)
            externalTaskService.complete(externalTask)
        } catch (e: Throwable) {
            externalTaskService.handleFailure(externalTask, "Something went wrong", Arrays.toString(e.stackTrace), 0, 0)
        }
    }
}
