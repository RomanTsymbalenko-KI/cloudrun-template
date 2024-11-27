package com.ki.app

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringbootApplicationTests : ShouldSpec({ extensions(SpringExtension) }) {

    init {
        context("starting the app") {
            should("work!"){

            }
        }
    }
}