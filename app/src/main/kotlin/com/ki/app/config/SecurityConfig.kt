package com.ki.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http {
            cors { }

            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }

            authorizeHttpRequests {
                authorize(HttpMethod.GET, "/health/**", permitAll)
                authorize(HttpMethod.GET, "/metrics/**", permitAll)

                // ReDoc URL specs
                authorize("/v3/api-docs/**", permitAll)
                authorize("/index.html", permitAll)

                authorize(anyRequest, permitAll)
            }

            // If your service needs to validate/consume JWTs, setup
            // an oauth2ResourceServer and configure in the application.properties
            // spring.security.oauth2.resourceserver.jwt.issuer-uri=https://ki-dev.eu.auth0.com/
            // spring.security.oauth2.resourceserver.jwt.audiences=https://your-auth0-service-api
            //  oauth2ResourceServer {
            //      jwt { }
            //  }
        }

        return http.build()
    }
}
