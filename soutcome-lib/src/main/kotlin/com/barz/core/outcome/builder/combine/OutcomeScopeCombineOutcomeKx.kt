@file:OptIn(ExperimentalTypeInference::class)

package com.barz.core.outcome.builder.combine

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.builder.OutcomeScope
import kotlin.experimental.ExperimentalTypeInference

context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A, B> combineOutcome(
    action1: () -> Outcome<A, Error>,
    action2: () -> Outcome<B, Error>,
    crossinline onSuccess: (A, B) -> Outcome<Success, Error>,
): Success =
    combineOutcome(
        action1,
        action2,
        action3 = OutcomeScope.unitBlock,
        action4 = OutcomeScope.unitBlock,
        action5 = OutcomeScope.unitBlock,
        action6 = OutcomeScope.unitBlock,
        onSuccess = { a, b, _, _, _, _ ->
            onSuccess(a, b)
        },
    )

context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A, B, C> combineOutcome(
    action1: () -> Outcome<A, Error>,
    action2: () -> Outcome<B, Error>,
    action3: () -> Outcome<C, Error>,
    crossinline onSuccess: (A, B, C) -> Outcome<Success, Error>,
): Success =
    combineOutcome(
        action1,
        action2,
        action3,
        action4 = OutcomeScope.unitBlock,
        action5 = OutcomeScope.unitBlock,
        action6 = OutcomeScope.unitBlock,
        onSuccess = { a, b, c, _, _, _ ->
            onSuccess(a, b, c)
        },
    )

context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A, B, C, D> combineOutcome(
    action1: () -> Outcome<A, Error>,
    action2: () -> Outcome<B, Error>,
    action3: () -> Outcome<C, Error>,
    action4: () -> Outcome<D, Error>,
    crossinline onSuccess: (A, B, C, D) -> Outcome<Success, Error>,
): Success =
    combineOutcome(
        action1,
        action2,
        action3,
        action4,
        action5 = OutcomeScope.unitBlock,
        action6 = OutcomeScope.unitBlock,
        onSuccess = { a, b, c, d, _, _ ->
            onSuccess(a, b, c, d)
        },
    )

context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A, B, C, D, E> combineOutcome(
    action1: () -> Outcome<A, Error>,
    action2: () -> Outcome<B, Error>,
    action3: () -> Outcome<C, Error>,
    action4: () -> Outcome<D, Error>,
    action5: () -> Outcome<E, Error>,
    crossinline onSuccess: (A, B, C, D, E) -> Outcome<Success, Error>,
): Success =
    combineOutcome(
        action1,
        action2,
        action3,
        action4,
        action5,
        action6 = OutcomeScope.unitBlock,
        onSuccess = { a, b, c, d, e, _ ->
            onSuccess(a, b, c, d, e)
        },
    )

context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A, B, C, D, E, F> combineOutcome(
    action1: () -> Outcome<A, Error>,
    action2: () -> Outcome<B, Error>,
    action3: () -> Outcome<C, Error>,
    action4: () -> Outcome<D, Error>,
    action5: () -> Outcome<E, Error>,
    action6: () -> Outcome<F, Error>,
    crossinline onSuccess: (A, B, C, D, E, F) -> Outcome<Success, Error>,
): Success {
    val a: Outcome.Success<A> = action1().successOrRaiseError()
    val b: Outcome.Success<B> = action2().successOrRaiseError()
    val c: Outcome.Success<C> = action3().successOrRaiseError()
    val d: Outcome.Success<D> = action4().successOrRaiseError()
    val e: Outcome.Success<E> = action5().successOrRaiseError()
    val f: Outcome.Success<F> = action6().successOrRaiseError()

    val outcome: Outcome<Success, Error> =
        onSuccess(
            a.value,
            b.value,
            c.value,
            d.value,
            e.value,
            f.value,
        )

    when (outcome) {
        is Outcome.Error -> raise(outcome.error)
        is Outcome.Success -> return outcome.value
    }
}