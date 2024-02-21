@file:Suppress("UNREACHABLE_CODE")

package com.barz.kotlin.core

import com.barz.core.outcome.builder.outcome
import com.barz.core.outcome.helpers.asOutcomeError
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class OutcomeScopeThrowableTest : FunSpec(
    body = {

        context("Simple result") {
            test("Expected exception during execute") {
                val outcome = outcome<Unit, String> {
                    registerCatch<IllegalAccessException> { exception ->
                        exception.message ?: ""
                    }

                    execute<Unit, Unit>(
                        action = { throw IllegalAccessException("TEST") },
                        onError = { throw IllegalStateException() },
                    )
                }

                outcome shouldBe "TEST".asOutcomeError()
            }

            test("Unexpected exception during execute") {
                val unexpectedException = IllegalAccessException("TEST")

                val result = runCatching {
                    val outcome = outcome<Unit, String> {
                        execute<Unit, Unit>(
                            action = { throw unexpectedException },
                            onError = { throw IllegalStateException() },
                        )
                    }

                    outcome shouldBe "TEST".asOutcomeError()
                }

                result.isFailure shouldBe true
                result.exceptionOrNull() shouldBe unexpectedException
            }
        }
    },
)