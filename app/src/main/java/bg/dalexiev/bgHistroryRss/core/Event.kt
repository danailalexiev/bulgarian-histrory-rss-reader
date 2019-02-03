package bg.dalexiev.bgHistroryRss.core

class Event<T>(private val payload: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null;
        } else {
            hasBeenHandled = true
            payload
        }
    }

    fun peek(): T = payload
}