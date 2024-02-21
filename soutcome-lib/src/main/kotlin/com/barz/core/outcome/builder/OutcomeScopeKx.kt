package com.barz.core.outcome.builder

import com.barz.core.outcome.Outcome

context(OutcomeScope<S, E>)
fun <S, E> Outcome<S, E>.returnOrRaise(): S {
    when (this) {
        is Outcome.Error -> raise(this.error)
        is Outcome.Success -> return this.value
    }
}