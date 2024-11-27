package com.ki.app

import com.ki.app.utils.GcpStringGenericConverter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.core.convert.support.DefaultConversionService

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
class SpringbootApplication

fun main(args: Array<String>) {
    val defaultConversionService = (DefaultConversionService.getSharedInstance() as? DefaultConversionService)
    defaultConversionService?.addConverter(GcpStringGenericConverter())
    runApplication<SpringbootApplication>(args = args)
}
