@file:Suppress("UNUSED_EXPRESSION", "UNREACHABLE_CODE")

package com.barz.kotlin.core

import com.barz.core.outcome.Outcome
import com.barz.core.outcome.builder.outcome
import com.barz.core.outcome.helpers.asOutcomeError
import com.barz.core.outcome.helpers.asOutcomeSuccess
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class OutcomeScopeExecuteTest : FunSpec(
    body = {

        context("Simple result") {
            test("Unit success") {
                val outcome = outcome<Unit, Unit> {
                    execute(
                        action = { Unit.asOutcomeSuccess() },
                        onError = { throw IllegalAccessException() },
                    ) shouldBe Unit
                }

                outcome shouldBe Unit.asOutcomeSuccess()
            }

            test("Int (42) success") {
                val outcome = outcome<Int, Unit> {
                    execute(
                        action = { 42.asOutcomeSuccess() },
                        onError = { throw IllegalAccessException() },
                    ) shouldBe 42
                }

                outcome shouldBe 42.asOutcomeSuccess()
            }

            test("Unit Error") {
                val outcome = outcome<Unit, Unit> {
                    execute<Unit, Unit>(
                        action = { Unit.asOutcomeError() },
                        onError = {
                            @Suppress("UNUSED_EXPRESSION")
                            it
                        },
                    )
                }

                outcome shouldBe Unit.asOutcomeError()
            }

            test("Unit Error mapped to Int") {
                val outcome = outcome<Unit, Int> {
                    execute(
                        action = { Unit.asOutcomeError() },
                        onError = { 42 },
                    )
                }

                outcome shouldBe 42.asOutcomeError()
            }
        }

        context("Combined executes") {
            context("Success") {
                test("Simple addition") {
                    outcome<Int, Unit> {
                        val firstInteger = execute(
                            action = { 42.asOutcomeSuccess() },
                            onError = { throw IllegalAccessException() },
                        )

                        firstInteger shouldBe 42

                        val secondInteger = execute(
                            action = { 43.asOutcomeSuccess() },
                            onError = { throw IllegalAccessException() },
                        )

                        secondInteger shouldBe 43

                        val thirdInteger = execute(
                            action = { 44.asOutcomeSuccess() },
                            onError = { throw IllegalAccessException() },
                        )

                        thirdInteger shouldBe 44

                        firstInteger + secondInteger + thirdInteger
                    } shouldBe 129.asOutcomeSuccess() // 42 + 43 + 44 = 129
                }
            }

            context("Error") {
                test("Simple addition - first fails") {
                    outcome<Int, Unit> {
                        val firstInteger = execute(
                            action = { Outcome.Error<Int, Unit>(errorValue = Unit) },
                            onError = { it },
                        )

                        val secondInteger = execute(
                            action = { 43.asOutcomeSuccess() },
                            onError = { throw IllegalAccessException() },
                        )

                        val thirdInteger = execute(
                            action = { 44.asOutcomeSuccess() },
                            onError = { throw IllegalAccessException() },
                        )

                        firstInteger + secondInteger + thirdInteger
                    } shouldBe Unit.asOutcomeError()
                }

                test("Simple addition - second fails") {
                    outcome<Int, Unit> {
                        val firstInteger = execute(
                            action = { 42.asOutcomeSuccess() },
                            onError = { throw IllegalAccessException() },
                        )

                        firstInteger shouldBe 42

                        val secondInteger = execute(
                            action = { Outcome.Error<Int, Unit>(errorValue = Unit) },
                            onError = { it },
                        )

                        val thirdInteger = execute(
                            action = { 44.asOutcomeSuccess() },
                            onError = { throw IllegalAccessException() },
                        )

                        firstInteger + secondInteger + thirdInteger
                    } shouldBe Unit.asOutcomeError()
                }

                test("Simple addition - third fails") {
                    outcome<Int, Unit> {
                        val firstInteger = execute(
                            action = { 42.asOutcomeSuccess() },
                            onError = { throw IllegalAccessException() },
                        )

                        firstInteger shouldBe 42

                        val secondInteger = execute(
                            action = { 43.asOutcomeSuccess() },
                            onError = { throw IllegalAccessException() },
                        )

                        secondInteger shouldBe 43

                        val thirdInteger = execute(
                            action = { Outcome.Error<Int, Unit>(errorValue = Unit) },
                            onError = { it },
                        )

                        firstInteger + secondInteger + thirdInteger
                    } shouldBe Unit.asOutcomeError()
                }
            }
        }

        context("Manual raise errors") {
            test("Directly from OutcomeScope") {
                outcome<Unit, Unit> {
                    raise(Unit)
                } shouldBe Unit.asOutcomeError()
            }

            test("Inside execute mapError") {
                val errorMessage = "Error manually raised."

                outcome<Int, String> {
                    execute<Int, String>(
                        action = { "Execute error".asOutcomeError() },
                        onError = {
                            raise(errorMessage)
                            "Not manually raised error message"
                        },
                    )
                } shouldBe errorMessage.asOutcomeError()
            }

            test("Even when successful") {
                val errorMessage = "Raised from success"

                outcome<Int, String> {
                    execute<Int, String>(
                        action = {
                            42.asOutcomeSuccess()
                            raise(errorMessage)
                        },
                        onError = { errorMessage -> errorMessage },
                    )
                } shouldBe errorMessage.asOutcomeError()
            }
        }
    },
)