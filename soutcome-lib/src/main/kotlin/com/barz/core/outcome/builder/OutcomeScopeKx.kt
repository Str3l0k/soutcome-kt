package com.barz.core.outcome.builder

import com.barz.core.outcome.Outcome

context(OutcomeScope<S, E>)
fun <S, E> Outcome<S, E>.returnOrRaise(): S {
    when (this) {
        is Outcome.Error -> raise(this.error)
        is Outcome.Success -> return this.value
    }
}

context(OutcomeScope<S2, E2>)
fun <S1, S2, E1, E2> Outcome<S1, E1>.onError(onError: (E1) -> E2): S1 {
    when (this) {
        is Outcome.Error -> raise(onError(this.error))
        is Outcome.Success -> return this.value
    }
}