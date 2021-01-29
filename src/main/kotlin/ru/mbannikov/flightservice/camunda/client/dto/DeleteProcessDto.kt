package ru.mbannikov.flightservice.camunda.client.dto

/** Тело запроса в Camunda для удаления процесса. */
data class DeleteProcessDto(
    val processInstanceQuery: ProcessInstanceQuery
)

data class ProcessInstanceQuery(

    /** Идентификатор процесса. */
    val businessKey: String
)
