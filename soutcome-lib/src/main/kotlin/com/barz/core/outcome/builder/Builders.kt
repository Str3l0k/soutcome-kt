package com.barz.core.outcome.builder

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.OutcomeErrorException

inline fun <reified S, reified E> outcome(block: OutcomeScope<S, E>.() -> S): Outcome<S, E> {
    val scope = OutcomeScope<S, E>()

    return try {
        val value: S = scope.block()
        Outcome.Success<S, E>(value)
    } catch (oec: OutcomeErrorException) {
        Outcome.Error<S, E>(oec.error as E)
    } catch (e: Throwable) {
        val selectedExceptionHandler = scope.getExceptionHandlerOrThrow(e)
        Outcome.Error<S, E>(selectedExceptionHandler(e))
    }
}