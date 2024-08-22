package com.barz.core.outcome.helpers

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.Outcome.Error
import com.barz.core.outcome.Outcome.Success

inline fun <
    SuccessIn,
    ErrorIn,
    SuccessOut,
    ErrorOut,
> Outcome<SuccessIn, ErrorIn>.flatMap(
    mapSuccess: (SuccessIn) -> SuccessOut,
    mapError: (ErrorIn) -> ErrorOut,
): Outcome<SuccessOut, ErrorOut> =
    when (this) {
        is Error -> mapError(this.error).asError()
        is Success -> mapSuccess(this.value).asSuccess()
    }

inline fun <
    Success,
    ErrorIn,
    ErrorOut,
> Outcome<Success, ErrorIn>.flatMapError(
    mapError: (ErrorIn) -> ErrorOut,
): Outcome<Success, ErrorOut> =
    flatMap(
        mapSuccess = { successValue -> successValue },
        mapError = mapError,
    )

inline fun <
    SuccessIn,
    SuccessOut,
    Error,
> Outcome<SuccessIn, Error>.flatMapSuccess(
    mapSuccess: (SuccessIn) -> SuccessOut,
): Outcome<SuccessOut, Error> =
    flatMap(
        mapSuccess = mapSuccess,
        mapError = { errorValue -> errorValue },
    )