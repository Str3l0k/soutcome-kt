package com.barz.core.outcome.builder.combine

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.builder.OutcomeScope
import com.barz.core.outcome.helpers.unwrap

context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A, B> combine(
    action1: CombineScope<Error>.() -> Outcome<A, Error>,
    action2: CombineScope<Error>.() -> Outcome<B, Error>,
    onSuccess: (A, B) -> Success,
): Success =
    combine(
        action1 = action1,
        action2 = action2,
        action3 = CombineScope.unitBlock(),
        action4 = CombineScope.unitBlock(),
        action5 = CombineScope.unitBlock(),
        action6 = CombineScope.unitBlock(),
        onSuccess = { a, b, _, _, _, _ ->
            onSuccess(a, b)
        },
    )

context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A, B, C> combine(
    action1: CombineScope<Error>.() -> Outcome<A, Error>,
    action2: CombineScope<Error>.() -> Outcome<B, Error>,
    action3: CombineScope<Error>.() -> Outcome<C, Error>,
    crossinline onSuccess: (A, B, C) -> Success,
): Success =
    combine(
        action1,
        action2,
        action3,
        action4 = CombineScope.unitBlock(),
        action5 = CombineScope.unitBlock(),
        action6 = CombineScope.unitBlock(),
        onSuccess = { a, b, c, _, _, _ ->
            onSuccess(a, b, c)
        },
    )

context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A, B, C, D> combine(
    action1: CombineScope<Error>.() -> Outcome<A, Error>,
    action2: CombineScope<Error>.() -> Outcome<B, Error>,
    action3: CombineScope<Error>.() -> Outcome<C, Error>,
    action4: CombineScope<Error>.() -> Outcome<D, Error>,
    crossinline onSuccess: (A, B, C, D) -> Success,
): Success =
    combine(
        action1,
        action2,
        action3,
        action4,
        action5 = CombineScope.unitBlock(),
        action6 = CombineScope.unitBlock(),
        onSuccess = { a, b, c, d, _, _ ->
            onSuccess(a, b, c, d)
        },
    )

context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A, B, C, D, E> combine(
    action1: CombineScope<Error>.() -> Outcome<A, Error>,
    action2: CombineScope<Error>.() -> Outcome<B, Error>,
    action3: CombineScope<Error>.() -> Outcome<C, Error>,
    action4: CombineScope<Error>.() -> Outcome<D, Error>,
    action5: CombineScope<Error>.() -> Outcome<E, Error>,
    crossinline onSuccess: (A, B, C, D, E) -> Success,
): Success =
    combine(
        action1,
        action2,
        action3,
        action4,
        action5,
        action6 = CombineScope.unitBlock(),
        onSuccess = { a, b, c, d, e, _ ->
            onSuccess(a, b, c, d, e)
        },
    )

context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A, B, C, D, E, F> combine(
    action1: CombineScope<Error>.() -> Outcome<A, Error>,
    action2: CombineScope<Error>.() -> Outcome<B, Error>,
    action3: CombineScope<Error>.() -> Outcome<C, Error>,
    action4: CombineScope<Error>.() -> Outcome<D, Error>,
    action5: CombineScope<Error>.() -> Outcome<E, Error>,
    action6: CombineScope<Error>.() -> Outcome<F, Error>,
    onSuccess: (A, B, C, D, E, F) -> Success,
): Success {
    val a: Outcome.Success<A> = action1(CombineScope()).successOrRaiseError()
    val b: Outcome.Success<B> = action2(CombineScope()).successOrRaiseError()
    val c: Outcome.Success<C> = action3(CombineScope()).successOrRaiseError()
    val d: Outcome.Success<D> = action4(CombineScope()).successOrRaiseError()
    val e: Outcome.Success<E> = action5(CombineScope()).successOrRaiseError()
    val f: Outcome.Success<F> = action6(CombineScope()).successOrRaiseError()

    return onSuccess(
        a.unwrap(),
        b.unwrap(),
        c.unwrap(),
        d.unwrap(),
        e.unwrap(),
        f.unwrap(),
    )
}

/**
 * Combine version where all success and error types
 * from executions are identical and therefore an undefined number of them
 * can be combined.
 */
context(OutcomeScope<Success, Error>)
inline fun <Success, Error, A> combine(
    vararg actions: CombineScope<Error>.() -> Outcome<A, Error>,
    onAllSuccess: (List<A>) -> Success,
): Success {
    val successes =
        actions.map { block ->
            block(CombineScope()).successOrRaiseError()
        }

    val values: List<A> =
        successes.map(
            com
                .barz
                .core
                .outcome
                .Outcome
                .Success<A>::unwrap,
        )

    return onAllSuccess(values)
}