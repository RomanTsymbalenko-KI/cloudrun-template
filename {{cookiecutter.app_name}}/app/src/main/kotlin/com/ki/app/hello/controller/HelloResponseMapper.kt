package com.ki.app.hello.controller

import com.ki.app.hello.controller.response.HelloResponse
import com.ki.app.hello.model.Handshake
import org.springframework.stereotype.Component

@Component
class HelloResponseMapper {

    fun mapToHelloResponse(handshake: Handshake): HelloResponse {
        return HelloResponse(
            id = handshake.id,
            name = handshake.name,
            extra = "tada!"
        )
    }
}
