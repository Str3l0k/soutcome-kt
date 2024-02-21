package com.barz.core.outcome.helpers

import com.barz.core.outcome.Outcome

inline fun <SuccessIn, ErrorIn, Out> Outcome<SuccessIn, ErrorIn>.fold(
    crossinline onSuccess: (SuccessIn) -> Out,
    crossinline onError: (ErrorIn) -> Out,
): Out =
    when (this) {
        is Outcome.Success -> onSuccess(this.value)
        is Outcome.Error -> onError(this.error)
    }