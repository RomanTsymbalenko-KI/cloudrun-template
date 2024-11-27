package com.ki.app.hello.controller.response

import java.util.UUID

data class HelloResponse(val id: UUID, val name: String, val extra: String)
