package ru.mbannikov.flightservice.camunda.client

import org.springframework.web.client.RestTemplate
import ru.mbannikov.flightservice.camunda.client.dto.CamundaValue
import ru.mbannikov.flightservice.camunda.client.dto.DeleteProcessDto
import ru.mbannikov.flightservice.camunda.client.dto.ProcessInstanceQuery
import ru.mbannikov.flightservice.camunda.client.dto.SendMessageDto
import ru.mbannikov.flightservice.camunda.client.dto.StartProcessDto
import ru.mbannikov.flightservice.utils.Logging

class CamundaClient(
    private val baseUrl: String,
    private val restTemplate: RestTemplate
) : Logging {

    fun startProcess(processKey: String, businessKey: String, variables: Map<String, CamundaValue<Any>> = emptyMap()) {
        log.info { "Start camunda process=$processKey with businessKey=$businessKey" }

        restTemplate.postForEntity(
            "$baseUrl/engine-rest/process-definition/key/$processKey/start",
            StartProcessDto(variables = variables, businessKey = businessKey),
            Unit::class.java
        )
    }

    fun sendMessage(messageName: String, businessKey: String, variables: Map<String, CamundaValue<Any>> = emptyMap()) {
        log.info { "Send message=$messageName to camunda with businessKey=$businessKey and variables=$variables" }

        restTemplate.postForEntity(
            "$baseUrl/engine-rest/message",
            SendMessageDto(messageName, businessKey, variables),
            Unit::class.java
        )
    }

    fun deleteProcess(businessKey: String) {
        log.info { "Delete camunda process with businessKey=$businessKey" }

        restTemplate.postForEntity(
            "$baseUrl/engine-rest/process-instance/delete",
            DeleteProcessDto(ProcessInstanceQuery(businessKey)),
            Unit::class.java
        )
    }
}
