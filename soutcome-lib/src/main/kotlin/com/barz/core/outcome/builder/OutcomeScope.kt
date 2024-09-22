package com.barz.core.outcome.builder

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.OutcomeErrorException
import com.barz.core.outcome.helpers.asOutcomeSuccess
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.InvocationKind.EXACTLY_ONCE
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
class OutcomeScope<S, E>
    @PublishedApi
    internal constructor() {
        companion object {
            @PublishedApi
            internal val unitBlock = { Unit.asOutcomeSuccess() }
        }

        @PublishedApi
        internal val exceptionHandler = mutableMapOf<String, (Throwable) -> E>()

        @PublishedApi
        internal inline fun <reified T : Throwable> getExceptionHandlerOrThrow(throwable: T): (T) -> E =
            exceptionHandler.getOrDefault(throwable::class.java.canonicalName) { throw throwable }

        inline fun validate(
            condition: () -> Boolean,
            onError: () -> E,
        ) {
            contract {
                callsInPlace(condition, InvocationKind.AT_MOST_ONCE)
                callsInPlace(onError, InvocationKind.AT_MOST_ONCE)
            }

            if (condition()) {
                return
            }

            raise(onError())
        }

        inline fun <U, V> execute(
            action: () -> Outcome<U, V>,
            onError: (V) -> E,
        ): U {
            contract {
                callsInPlace(action, InvocationKind.AT_MOST_ONCE)
                callsInPlace(onError, InvocationKind.AT_MOST_ONCE)
            }

            return when (val outcome: Outcome<U, V> = action()) {
                is Outcome.Error -> raise(onError(outcome.error))
                is Outcome.Success -> outcome.value
            }
        }

        inline fun <U> execute(action: () -> Outcome<U, E>): U {
            contract {
                callsInPlace(action, EXACTLY_ONCE)
            }

            return when (val outcome: Outcome<U, E> = action()) {
                is Outcome.Error -> raise(outcome.error)
                is Outcome.Success -> outcome.value
            }
        }

        @Suppress("UNCHECKED_CAST")
        inline fun <reified T : Throwable> registerCatch(noinline onCatch: (T) -> E) {
            exceptionHandler[T::class.java.canonicalName] = onCatch as (Throwable) -> E
        }

        /**
         * Shortcut to an outcome error.
         * If an error is raised, everything after this call will not be executed anymore,
         * but instead the outcome builder call will return this error value
         * as Outcome.Error directly.
         */
        fun raise(error: E): Nothing = throw OutcomeErrorException(error)

        @PublishedApi
        internal fun <S> Outcome<S, E>.successOrRaiseError(): Outcome.Success<S> {
            contract { returns() implies (this@successOrRaiseError is Outcome.Success<S>) }

            return when (this) {
                is Outcome.Error -> raise(this.error)
                is Outcome.Success -> this
            }
        }

        fun <S1, E1> Outcome<S1, E1>.raiseOnError(onError: (E1) -> E): S1 {
            when (this) {
                is Outcome.Error -> raise(onError(this.error))
                is Outcome.Success -> return this.value
            }
        }

        fun Outcome<S, E>.returnOrRaise(): S {
            when (this) {
                is Outcome.Error -> raise(this.error)
                is Outcome.Success -> return this.value
            }
        }
    }