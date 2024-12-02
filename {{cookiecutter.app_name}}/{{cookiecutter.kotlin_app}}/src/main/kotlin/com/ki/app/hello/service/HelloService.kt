package com.ki.app.hello.service

import com.ki.app.hello.model.Handshake
import com.ki.app.hello.repository.HelloRepository
import org.springframework.stereotype.Service

@Service
class HelloService(private val helloRepository: HelloRepository) {

    fun shakeHand(): Handshake = helloRepository.findHandshake()
}
