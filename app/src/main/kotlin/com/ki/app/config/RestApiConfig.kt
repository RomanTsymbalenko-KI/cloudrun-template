package com.ki.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient


@Configuration
class RestApiConfig {

    @Bean
    fun restClient(restClientBuilder: RestClient.Builder): RestClient {
        return restClientBuilder.build()
    }
}