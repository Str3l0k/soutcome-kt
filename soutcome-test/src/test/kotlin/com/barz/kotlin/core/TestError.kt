package com.barz.kotlin.core

sealed class TestError {
    data object One : TestError()
    data object Two : TestError()
    data object Three : TestError()
    data object Four : TestError()
    data object Five : TestError()
    data object Six : TestError()

    data class Data(
        val value: String,
    ) : TestError()

    companion object {
        fun allObjectErrors(): List<TestError> =
            listOf(
                One,
                Two,
                Three,
                Four,
                Five,
                Six,
            )

        fun allObjectErrorsWithBlock(): List<Pair<Int, () -> TestError>> =
            allObjectErrors()
                .mapIndexed { index, testError ->
                    index to { testError }
                }
    }
}