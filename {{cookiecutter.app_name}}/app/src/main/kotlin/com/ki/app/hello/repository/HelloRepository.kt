package com.ki.app.hello.repository

import com.ki.app.hello.model.Handshake
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class HelloRepository {
    fun findHandshake(): Handshake {
        return Handshake(UUID.randomUUID(), "Hello!")
    }
}
