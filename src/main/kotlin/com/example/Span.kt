package com.example

import io.opentracing.Span
import io.opentracing.SpanContext
import io.opentracing.util.GlobalTracer

inline fun <T> span(name: String, parentContext: SpanContext? = null, block: Span.() -> T): T {
    val tracer = GlobalTracer.get()
    val spanBuilder = tracer.buildSpan(name)
    if (parentContext != null) {
        spanBuilder.asChildOf(parentContext)
    }

    val span = spanBuilder.start()

    try {
        tracer.scopeManager().activate(span).use {
            return block(span)
        }
    } finally {
        span.finish()
    }
}
