package com.barz.core.outcome

@PublishedApi
internal class OutcomeErrorException(
    val error: Any?,
) : Exception()