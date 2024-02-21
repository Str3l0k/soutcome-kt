package com.barz.core.outcome.builder.accumulate

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.builder.OutcomeScope
import com.barz.core.outcome.helpers.flatMapError
import com.barz.core.outcome.helpers.process
import com.barz.core.outcome.helpers.unwrap

context(OutcomeScope<Success, List<Error>>)
inline fun <Success, Error, A, B> AccumulateScope<Success, Error>.combineOrAccumulate(
    block1: () -> Outcome<A, Error>,
    block2: () -> Outcome<B, Error>,
    onSuccess: (A, B) -> Outcome<Success, Error>,
): Outcome<Success, List<Error>> =
    combineOrAccumulate(
        block1 = block1,
        block2 = block2,
        block3 = OutcomeScope.unitBlock,
        block4 = OutcomeScope.unitBlock,
        block5 = OutcomeScope.unitBlock,
        combineBlock = { a, b, _, _, _ ->
            onSuccess(a, b)
        },
    )

context(OutcomeScope<Success, List<Error>>)
inline fun <Success, Error, A, B, C> AccumulateScope<Success, Error>.combineOrAccumulate(
    block1: () -> Outcome<A, Error>,
    block2: () -> Outcome<B, Error>,
    block3: () -> Outcome<C, Error>,
    onSuccess: (A, B, C) -> Outcome<Success, Error>,
): Outcome<Success, List<Error>> =
    combineOrAccumulate(
        block1 = block1,
        block2 = block2,
        block3 = block3,
        block4 = OutcomeScope.unitBlock,
        block5 = OutcomeScope.unitBlock,
        combineBlock = { a, b, c, _, _ ->
            onSuccess(a, b, c)
        },
    )

context(OutcomeScope<Success, List<Error>>)
inline fun <Success, Error, A, B, C, D> AccumulateScope<Success, Error>.combineOrAccumulate(
    block1: () -> Outcome<A, Error>,
    block2: () -> Outcome<B, Error>,
    block3: () -> Outcome<C, Error>,
    block4: () -> Outcome<D, Error>,
    onSuccess: (A, B, C, D) -> Outcome<Success, Error>,
): Outcome<Success, List<Error>> =
    combineOrAccumulate(
        block1 = block1,
        block2 = block2,
        block3 = block3,
        block4 = block4,
        block5 = OutcomeScope.unitBlock,
        combineBlock = { a, b, c, d, _ ->
            onSuccess(a, b, c, d)
        },
    )

context(OutcomeScope<Success, List<Error>>)
inline fun <Success, Error, A, B, C, D, E> AccumulateScope<Success, Error>.combineOrAccumulate(
    block1: () -> Outcome<A, Error>,
    block2: () -> Outcome<B, Error>,
    block3: () -> Outcome<C, Error>,
    block4: () -> Outcome<D, Error>,
    block5: () -> Outcome<E, Error>,
    combineBlock: (A, B, C, D, E) -> Outcome<Success, Error>,
): Outcome<Success, List<Error>> {
    val errors: MutableList<Error> = mutableListOf()

    val a: Outcome<A, Error> = block1()
    val b: Outcome<B, Error> = block2()
    val c: Outcome<C, Error> = block3()
    val d: Outcome<D, Error> = block4()
    val e: Outcome<E, Error> = block5()

    a.process(onError = errors::add)
    b.process(onError = errors::add)
    c.process(onError = errors::add)
    d.process(onError = errors::add)
    e.process(onError = errors::add)

    if (errors.isNotEmpty()) {
        raiseAccumulate(errors)
        return errors.asErrorScoped()
    }

    val combineOutcome: Outcome<Success, Error> =
        combineBlock(
            a.unwrap(),
            b.unwrap(),
            c.unwrap(),
            d.unwrap(),
            e.unwrap(),
        )

    return combineOutcome
        .flatMapError(::listOf)
}