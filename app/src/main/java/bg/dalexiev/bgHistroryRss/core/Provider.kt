package bg.dalexiev.bgHistroryRss.core

abstract class Provider<T> {

    var mock: T? = null
    var original: T? = null

    abstract fun create(): T

    fun get(): T = mock ?: original ?: create().apply { original = this }

    fun lazyGet(): Lazy<T> = lazy { get() }
}