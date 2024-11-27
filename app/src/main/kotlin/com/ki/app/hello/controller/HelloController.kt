package com.ki.app.hello.controller

import com.ki.app.hello.controller.response.HelloResponse
import com.ki.app.hello.service.HelloService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
class HelloController(
    private val helloService: HelloService,
    private val helloDtoMapper: HelloResponseMapper
) {

    @GetMapping
    fun shakeHands(): HelloResponse {
        val forms = helloService.shakeHand()
        return helloDtoMapper.mapToHelloResponse(forms)
    }
}
