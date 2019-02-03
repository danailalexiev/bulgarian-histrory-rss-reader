package bg.dalexiev.bgHistroryRss.core

sealed class State<out T> {

    class Loading<out T> : State<T>()

    class Success<out T>(val value: T) : State<T>()

    class Failure<out T>(val error: Throwable) : State<T>()

    companion object {

        fun <T> loading() = Loading<T>()

        fun <T> success(value: T) = Success(value)

        fun <T> failure(throwable: Throwable) = Failure<T>(throwable)
    }

}