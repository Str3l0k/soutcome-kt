package com.barz.core.outcome.builder.accumulate

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.builder.OutcomeScope
import com.barz.core.outcome.helpers.asError
import com.barz.core.outcome.helpers.asSuccess

/**
 * This function is used for executing and/or validating multiple
 * actions/conditions without using the success values produced by these statements.
 *
 * **Important:**
 * Errors accumulated are **not** raised automatically.
 * Call *accumulateErrorsAndRaise()* if automatic raising accumulated errors is wanted.
 *
 * **Returns** outcome with either success if no errors occurred or all accumulated errors
 * as List.
 */
@Suppress("UnusedReceiverParameter")
inline fun <Success, Error> OutcomeScope<Success, List<Error>>.accumulateErrors(
    block: AccumulateScope<Success, Error>.() -> Success,
): Outcome<Success, List<Error>> {
    val scope = AccumulateScope<Success, Error>()
    val successValue = scope.block()

    return if (scope.errors.isNotEmpty()) {
        scope.errors.asError()
    } else {
        successValue.asSuccess()
    }
}

/**
 * This function is used for executing and/or validating multiple
 * actions/conditions without using the success values produced by these statements.
 *
 * **Important:**
 * Errors accumulated are raised automatically.
 * Call *accumulateErrors()* if automatic raising accumulated errors is not wanted.
 *
 * **Returns** Success value if no errors occurred, otherwise raises the error list.
 */
inline fun <Success, Error> OutcomeScope<Success, List<Error>>.accumulateErrorsAndRaise(
    block: AccumulateScope<Success, Error>.() -> Success,
): Success = accumulateErrors(block).returnOrRaise()