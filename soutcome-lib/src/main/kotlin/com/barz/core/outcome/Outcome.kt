package com.barz.core.outcome

import com.barz.core.outcome.helpers.asError
import com.barz.core.outcome.helpers.asSuccess

sealed class Outcome<out SuccessType, out ErrorType> {
    @ConsistentCopyVisibility
    data class Success<SuccessType> internal constructor(
        val value: SuccessType,
    ) : Outcome<SuccessType, Nothing>() {
        override fun toString(): String =
            """
            Outcome.{Success}<${value?.let { it::class.simpleName }}> with value {$value}
            """.trimIndent()

        companion object
    }

    @ConsistentCopyVisibility
    data class Error<out ErrorType> internal constructor(
        val error: ErrorType,
    ) : Outcome<Nothing, ErrorType>() {
        override fun toString(): String =
            """
            Outcome.{Error}<${error?.let { it::class.simpleName }}> with value {$error}
            """.trimIndent()

        companion object
    }

    // =================================================

    companion object {
        val SuccessUnit = Unit.asSuccess()
        val ErrorUnit = Unit.asError()

        @Suppress("FunctionName")
        fun <S, E> Success(successValue: S): Outcome<S, E> = Success(value = successValue)

        @Suppress("FunctionName")
        fun <S, E> Error(errorValue: E): Outcome<S, E> = Error(error = errorValue)
    }

    // =================================================
}