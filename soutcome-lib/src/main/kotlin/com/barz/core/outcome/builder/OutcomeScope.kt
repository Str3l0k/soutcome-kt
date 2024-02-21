package com.barz.core.outcome.builder

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.OutcomeErrorException
import com.barz.core.outcome.helpers.asOutcomeSuccess
import kotlin.contracts.ExperimentalContracts
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
                callsInPlace(condition, EXACTLY_ONCE)
                callsInPlace(onError, EXACTLY_ONCE)
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
                callsInPlace(action, EXACTLY_ONCE)
                callsInPlace(onError, EXACTLY_ONCE)
            }

            return when (val outcome: Outcome<U, V> = action()) {
                is Outcome.Error -> raise(onError(outcome.error))
                is Outcome.Success -> outcome.value
            }
        }

        @Suppress("UNCHECKED_CAST")
        inline fun <reified T : Throwable> registerCatch(noinline onCatch: (T) -> E) {
            exceptionHandler[T::class.java.canonicalName] = onCatch as (Throwable) -> E
        }

        /**
         * Shortcut to an outcome error.
         * If a error is raised, everything after this call will not be executed anymore,
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

        fun S.asSuccessScoped(): Outcome<S, E> = Outcome.Success(value = this)

        fun E.asErrorScoped(): Outcome<S, E> = Outcome.Error(this)
    }