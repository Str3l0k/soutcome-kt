package com.barz.core.outcome.helpers

import com.barz.core.outcome.Outcome

inline fun <SuccessIn, ErrorIn, Out> Outcome<SuccessIn, ErrorIn>.fold(
    onSuccess: (SuccessIn) -> Out,
    onError: (ErrorIn) -> Out,
): Out =
    when (this) {
        is Outcome.Success -> onSuccess(this.value)
        is Outcome.Error -> onError(this.error)
    }