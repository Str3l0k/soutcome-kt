package com.barz.core.outcome.helpers

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.Outcome.Companion.ErrorUnit
import com.barz.core.outcome.Outcome.Companion.SuccessUnit
import com.barz.core.outcome.Outcome.Error
import com.barz.core.outcome.Outcome.Success
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <S, E> Outcome<S, E>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Success)
        returns(false) implies (this@isSuccess is Error)
    }

    return this is Success
}

@OptIn(ExperimentalContracts::class)
fun <S, E> Outcome<S, E>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is Error<E>)
        returns(false) implies (this@isError is Success<S>)
    }

    return this is Error
}

fun <S, E> Outcome<S, E>.unwrap(): S = (this as Success).value

fun <S> S.asSuccess(): Outcome<S, Nothing> = Outcome.Success(successValue = this)

fun <E> E.asError(): Outcome<Nothing, E> = Outcome.Error(errorValue = this)

fun <S, E> S.asTypedSuccess(): Outcome<S, E> = Outcome.Success(successValue = this)

fun <S, E> E.asTypedError(): Outcome<S, E> = Outcome.Error(errorValue = this)

fun <S> S.asOutcomeSuccess(): Outcome<S, Nothing> = this.asSuccess()

fun <E> E.asOutcomeError(): Outcome<Nothing, E> = this.asError()

@Suppress("RemoveRedundantQualifierName")
fun Outcome.Success.Companion.unit() = SuccessUnit

@Suppress("RemoveRedundantQualifierName")
fun Outcome.Error.Companion.unit() = ErrorUnit