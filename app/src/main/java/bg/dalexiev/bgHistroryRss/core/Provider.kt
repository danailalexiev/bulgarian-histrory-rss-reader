package bg.dalexiev.bgHistroryRss.core

abstract class Provider<T, in P> {

    var mock: T? = null
    var original: T? = null

    abstract fun create(param: P): T

    fun get(param: P): T = mock ?: original ?: create(param).also { original = it }

    fun lazyGet(param: P): Lazy<T> = lazy { get(param) }
}