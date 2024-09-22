@file:Suppress("RedundantUnitExpression")

package com.barz.kotlin.core

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.builder.accumulate.accumulateErrors
import com.barz.core.outcome.builder.accumulate.accumulateErrorsAndRaise
import com.barz.core.outcome.builder.outcome
import com.barz.core.outcome.helpers.asError
import com.barz.core.outcome.helpers.asSuccess
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class OutcomeScopeAccumulateErrorsTest : FunSpec(
    body = {

        val errorValue = 42
        val errorOutcome = errorValue.asError()

        test("Simple execute errors") {
            val outcome = outcome<Unit, List<Int>> {
                accumulateErrors {
                    executeOrAccumulate { errorOutcome }
                    executeOrAccumulate { errorOutcome }
                    executeOrAccumulate { errorOutcome }
                }.returnOrRaise()
            }

            outcome shouldBe Outcome.Error(errorValue = List(3) { errorValue })
        }

        test("Simple validate errors") {
            val outcome = outcome<Unit, List<Int>> {
                accumulateErrors {
                    validateOrAccumulate({ false }, { errorValue })
                    validateOrAccumulate({ false }, { errorValue })
                    validateOrAccumulate({ false }, { errorValue })
                }.returnOrRaise()
            }

            outcome shouldBe Outcome.Error(errorValue = List(3) { errorValue })
        }

        test("Combined execute, validate, manual raised errors") {
            val outcome = outcome<Unit, List<Int>> {
                accumulateErrorsAndRaise {
                    executeOrAccumulate { errorOutcome }
                    validateOrAccumulate({ false }, { errorValue })
                    raiseAccumulate(errorValue)
                    raiseAccumulate(listOf(errorValue, errorValue))
                }
            }

            outcome shouldBe Outcome.Error(errorValue = List(5) { errorValue })
        }

        test("No errors return success") {
            val successValue = 44

            val outcome = outcome {
                accumulateErrorsAndRaise {
                    executeOrAccumulate { 43.asSuccess() }
                    validateOrAccumulate(
                        condition = { true },
                        onError = { Unit },
                    )

                    successValue
                }
            }

            outcome shouldBe successValue.asSuccess()
        }
    },
)