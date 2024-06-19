package com.example.plugins

import com.example.span
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withTimeout

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/test") {
            span("test") {
                call.respondText("Test")
            }
        }
        get("/testflow") {
            val f = flow {
                span("insideFlowSpan") {
                    println("insideFlowSpan")
                }
                emit(1)
            }.flowOn(Dispatchers.IO)
            val ff = f.single()
            // This is broken with the coroutine flag
            // NOT broken if withOUT
            span("outsideFlowSpan") {
                println("hello $ff")
            }
            call.respond("ok")
        }

        get("/testflow2") {
            val f = flow {
                span("insideFlowSpan") {
                    println("insideFlowSpan 2")
                }
                emit(1)
            }
            val ff = f.single()
            // This is not broken
            // in both with/without the flag cases
            span("outsideFlowSpan") {
                println("hello $ff")
            }
            call.respond("ok")
        }

        get("/testtimeout") {
            span("beforeTimeout") {
                delay(10)
            }
            withTimeout(50) {
                span("insideTimeout") {
                    delay(10)
                }
            }
            // This is detached
            span("afterTimeout") {
                delay(10)
            }
            span("afterTimeout2") {
                delay(10)
            }
            span("afterTimeout3") {
                delay(10)
            }
            call.respond("ok")
        }

    }
}
