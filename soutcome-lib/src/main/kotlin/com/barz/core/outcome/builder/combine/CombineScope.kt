package com.barz.core.outcome.builder.combine

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.helpers.asOutcomeSuccess

class CombineScope<E>
    @PublishedApi
    internal constructor() {
        companion object {
            @PublishedApi
            internal fun <E> unitBlock(): CombineScope<E>.() -> Outcome<Unit, Nothing> = { Unit.asOutcomeSuccess() }
        }
    }