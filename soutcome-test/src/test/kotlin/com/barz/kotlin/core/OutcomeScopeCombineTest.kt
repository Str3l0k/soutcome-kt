package com.barz.kotlin.core

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.builder.combine.combine
import com.barz.core.outcome.builder.outcome
import com.barz.core.outcome.helpers.asError
import com.barz.core.outcome.helpers.asOutcomeError
import com.barz.core.outcome.helpers.asOutcomeSuccess
import com.barz.core.outcome.helpers.asSuccess
import com.barz.kotlin.core.TestError.Five
import com.barz.kotlin.core.TestError.Four
import com.barz.kotlin.core.TestError.One
import com.barz.kotlin.core.TestError.Six
import com.barz.kotlin.core.TestError.Three
import com.barz.kotlin.core.TestError.Two
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe

class OutcomeScopeCombineTest : FunSpec(
    body = {

        context("Multiple errors - shortcut to first error") {
            test("Four to Six are errors") {
                val outcome: Outcome<Unit, TestError> = outcome {
                    combine(
                        action1 = { One.asSuccess() },
                        action2 = { Two.asSuccess() },
                        action3 = { Three.asSuccess() },
                        action4 = { Four.asError() },
                        action5 = { Five.asError() },
                        action6 = { Six.asError() },
                        onSuccess = { _, _, _, _, _, _ ->
                            throw IllegalAccessException()
                        },
                    )
                }

                outcome shouldBe TestError.Four.asOutcomeError()
            }

            test("All errors (6)") {
                val outcome: Outcome<Unit, TestError> = outcome {
                    combine(
                        action1 = { One.asError() },
                        action2 = { Two.asError() },
                        action3 = { Three.asError() },
                        action4 = { Four.asError() },
                        action5 = { Five.asError() },
                        action6 = { Six.asError() },
                        onSuccess = { _, _, _, _, _, _ ->
                            throw IllegalAccessException()
                        },
                    )
                }

                outcome shouldNotBeIn arrayOf(
                    TestError.Two,
                    TestError.Three,
                    TestError.Four,
                    TestError.Five,
                    TestError.Six,
                )

                outcome shouldBe Outcome.Error(TestError.One)
            }
        }

        context("Single Error - shortcut") {
            fun returnErrorWhenMatching(
                currentTestError: TestError,
                resultTestError: TestError,
            ): Outcome<Unit, TestError> =
                if (resultTestError == currentTestError) {
                    currentTestError.asError()
                } else {
                    Unit.asOutcomeSuccess()
                }

            withData(
                nameFn = { "Return TestError.[$it] as Outcome" },
                ts = TestError.allObjectErrors(),
            ) { testError ->
                outcome<Unit, TestError> {
                    combine(
                        action1 = { returnErrorWhenMatching(One, testError) },
                        action2 = { returnErrorWhenMatching(Two, testError) },
                        action3 = { returnErrorWhenMatching(Three, testError) },
                        action4 = { returnErrorWhenMatching(Four, testError) },
                        action5 = { returnErrorWhenMatching(Five, testError) },
                        action6 = { returnErrorWhenMatching(Six, testError) },
                        onSuccess = { _, _, _, _, _, _ ->
                            throw IllegalAccessException()
                        },
                    )
                } shouldBe testError.asOutcomeError()
            }

            test("Nothing is executed after error - ensure shortcut is used") {
                outcome<Unit, TestError> {
                    combine(
                        action1 = { Unit.asSuccess() },
                        action2 = { Two.asError() },
                        action3 = { throw IllegalAccessException() },
                        action4 = { throw IllegalAccessException() },
                        action5 = { throw IllegalAccessException() },
                        action6 = { throw IllegalAccessException() },
                        onSuccess = { _, _, _: Nothing, _: Nothing, _: Nothing, _: Nothing ->
                            throw IllegalAccessException()
                        },
                    )
                } shouldBe TestError.Two.asOutcomeError()
            }
        }
    },
)