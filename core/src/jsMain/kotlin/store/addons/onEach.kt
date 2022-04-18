package de.peekandpoke.kraft.store.addons

import de.peekandpoke.kraft.store.Stream
import de.peekandpoke.kraft.store.StreamWrapper

fun <T> Stream<T>.onEach(block: (T) -> Unit): Stream<T> =
    StreamOnEachOperator(wrapped = this, block = block)

private class StreamOnEachOperator<T>(
    wrapped: Stream<T>,
    private val block: (T) -> Unit
) : StreamWrapper<T>(wrapped = wrapped) {

    override fun handleIncoming(value: T) {

        block(value)

        super.handleIncoming(value)
    }
}
