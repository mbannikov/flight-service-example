package ru.mbannikov.flightservice.camunda

import org.camunda.bpm.client.ExternalTaskClient
import org.camunda.bpm.client.task.ExternalTask
import org.camunda.bpm.client.task.ExternalTaskService
import org.camunda.bpm.client.topic.TopicSubscriptionBuilder
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

abstract class AbstractCamundaWorker(
    private val topicName: String,
    private val lockDuration: Long = 5000,
    private val subscriptionConfigurer: TopicSubscriptionBuilder.() -> TopicSubscriptionBuilder = { this }
) {
    @Autowired
    private lateinit var client: ExternalTaskClient

    @PostConstruct
    protected fun init() {
        client
            .subscribe(topicName)
            .lockDuration(lockDuration)
            .handler { externalTask, externalTaskService ->
                doWork(externalTask, externalTaskService)
            }
            .subscriptionConfigurer()
            .open()
    }

    @PreDestroy
    protected fun destroy() {
        client.stop()
    }

    abstract fun doWork(externalTask: ExternalTask, externalTaskService: ExternalTaskService)
}
