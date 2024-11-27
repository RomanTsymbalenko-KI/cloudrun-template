package com.ki.app.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configures the default Object Mapper to be injected.
 */
@Configuration
class ObjectMapperConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        val om = ObjectMapper()
        om.registerModule(kotlinModule())
        om.registerModule(JavaTimeModule())

        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        return om
    }
}
