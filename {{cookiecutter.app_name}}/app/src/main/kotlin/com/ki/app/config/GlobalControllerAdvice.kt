package com.ki.app.config

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalControllerAdvice : ResponseEntityExceptionHandler() {

    override fun handleExceptionInternal(
        ex: java.lang.Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        // if body is not provided, use the exception message
        return super.handleExceptionInternal(
            ex,
            KiErrorResponse(statusCode.value(), (body ?: ex.localizedMessage).toString()),
            headers,
            statusCode,
            request
        ) ?: error("couldn't handle exception internally")
    }

    /**
     * General Ki error response for unhandled exceptions.
     */
    data class KiErrorResponse(
        val statusCode: Int,
        val error: String
    )
}
