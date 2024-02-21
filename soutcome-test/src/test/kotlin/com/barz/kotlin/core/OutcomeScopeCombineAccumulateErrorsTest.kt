package com.barz.kotlin.core

import com.barz.core.outcome.builder.accumulate.accumulateErrors
import com.barz.core.outcome.builder.accumulate.combineOrAccumulate
import com.barz.core.outcome.builder.outcome
import com.barz.core.outcome.builder.returnOrRaise
import com.barz.core.outcome.helpers.asError
import com.barz.core.outcome.helpers.asSuccess
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class OutcomeScopeCombineAccumulateErrorsTest : FunSpec(
    body = {

        test("Multiple errors accumulated with different calls") {
            outcome {
                val accumulateErrors = accumulateErrors {
                    validateOrAccumulate(
                        condition = { false },
                        onError = { "ER1" },
                    )

                    validateOrAccumulate(
                        condition = { false },
                        onError = { "ER2" },
                    )

                    validateOrAccumulate(
                        condition = { false },
                        onError = { "ER3" },
                    )

                    @Suppress("UNUSED_VARIABLE")
                    val x = combineOrAccumulate(
                        block1 = { "42".asError() },
                        block2 = { 42.asSuccess() },
                        onSuccess = { a, _ ->
                            a
                        },
                    )

                    executeOrAccumulate {
                        "Test".asError()
                    }

                    raiseAccumulate("Acc")

                    42
                }

                accumulateErrors.returnOrRaise()
            } shouldBe listOf(
                "ER1",
                "ER2",
                "ER3",
                "42",
                "Test",
                "Acc",
            ).asError()
        }
    },
)