package ru.mbannikov.flightservice.camunda

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.camunda.bpm.client.ExternalTaskClient
import org.camunda.bpm.client.backoff.ExponentialBackoffStrategy
import org.camunda.bpm.client.impl.ExternalTaskClientBuilderImpl
import org.camunda.bpm.client.spi.DataFormatConfigurator
import org.camunda.bpm.client.variable.impl.format.json.JacksonJsonDataFormat
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class CamundaConfiguration {

    @Bean
    fun externalTaskClient(): ExternalTaskClient =
        KotlinExternalTaskClient.create()
            .baseUrl("http://localhost:8080/engine-rest")
            .maxTasks(1)
            .asyncResponseTimeout(1000)
            .backoffStrategy(ExponentialBackoffStrategy(0, 0F, 0))
            .build()

    @Bean
    fun camundaRestTemplate(): RestTemplate {
        return RestTemplateBuilder().build()
    }
}

private class KotlinExternalTaskClient : ExternalTaskClientBuilderImpl() {
    override fun initObjectMapper() {
        super.initObjectMapper()
        JacksonDataFormatClientConfigurator.configureObjectMapper(super.objectMapper)
    }

    companion object {
        @JvmStatic
        fun create() = KotlinExternalTaskClient()
    }
}

/**
 * Настройка кастомного ObjectMapper'а для корректной десериализации котлиновских data классов.
 * Данный класс должен быть указан в файле "resources/META-INF/services/org.camunda.bpm.client.spi.DataFormatConfigurator".
 */
class JacksonDataFormatClientConfigurator : DataFormatConfigurator<JacksonJsonDataFormat> {

    override fun configure(dataFormat: JacksonJsonDataFormat) {
        configureObjectMapper(dataFormat.objectMapper)
    }

    override fun getDataFormatClass(): Class<JacksonJsonDataFormat> = JacksonJsonDataFormat::class.java

    companion object {
        fun configureObjectMapper(objectMapper: ObjectMapper): ObjectMapper =
            objectMapper.apply {
                registerModule(KotlinModule())
                registerModule(Jdk8Module())
                registerModule(JavaTimeModule())
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            }
    }
}
