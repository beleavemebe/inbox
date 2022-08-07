package io.github.beleavemebe.inbox.tasks.domain

import org.junit.Assert

object AssertExt {
    inline fun <reified E> assertListEquals(expected: List<E>, actual: List<E>) =
        Assert.assertArrayEquals(expected.toTypedArray(), actual.toTypedArray())
}
