package com.barz.core.outcome.builder.accumulate

import com.barz.core.outcome.Outcome

class AccumulateScope<Success, Error>
    @PublishedApi
    internal constructor() {
        @PublishedApi
        internal val errors: MutableList<Error> = mutableListOf()

        inline fun validateOrAccumulate(
            crossinline condition: () -> Boolean,
            crossinline onError: () -> Error,
        ) {
            if (condition().not()) {
                errors.add(onError())
            }
        }

        inline fun <Success> executeOrAccumulate(crossinline action: () -> Outcome<Success, Error>) {
            when (val outcome = action()) {
                is Outcome.Error -> errors.add(outcome.error)
                is Outcome.Success -> Unit
            }
        }

        fun raiseAccumulate(error: Error) {
            errors.add(error)
        }

        fun raiseAccumulate(errors: List<Error>) {
            this.errors.addAll(errors)
        }
    }