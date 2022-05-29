package com.github.caay2000.ttk.lib.context

import arrow.core.Either
import arrow.core.Option
import arrow.core.right

fun <LEFT, RIGHT_A, RIGHT_B> Either<LEFT, RIGHT_A>.context(first: RIGHT_B): Either<LEFT, Pair<RIGHT_B, RIGHT_A>> = this.map { first to it }

inline fun <A, B, C> Either<A, B>.flatNap(f: (B) -> Either<A, C>): Either<A, Pair<B, C>> =
    when (this) {
        is Either.Right -> f(this.value).context(this.value)
        is Either.Left -> this
    }

inline fun <A, B, C> Either<A, B>.flatOption(f: (B) -> Option<C>): Either<A, Pair<B, C?>> =
    when (this) {
        is Either.Right -> f(this.value).let { res -> this.value to res.orNull() }.right()
        is Either.Left -> this
    }
