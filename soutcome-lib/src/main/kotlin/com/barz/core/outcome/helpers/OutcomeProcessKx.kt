package com.barz.core.outcome.helpers

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.Outcome.Error
import com.barz.core.outcome.Outcome.Success

inline fun <SuccessIn, ErrorIn> Outcome<SuccessIn, ErrorIn>.process(
    onBoth: (() -> Unit) = { },
    onError: (ErrorIn) -> Unit = { },
    onSuccess: (SuccessIn) -> Unit = { },
) {
    when (this) {
        is Success -> onSuccess(this.value)
        is Error -> onError(this.error)
    }

    onBoth()
}