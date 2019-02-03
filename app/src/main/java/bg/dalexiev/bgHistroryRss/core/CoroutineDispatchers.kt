package bg.dalexiev.bgHistroryRss.core

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object CoroutineDispatchers {

    @JvmStatic
    var defaultProvider: (() -> CoroutineContext) -> CoroutineContext = { it() }

    @JvmStatic
    val default = defaultProvider { Dispatchers.Default }

    @JvmStatic
    var mainProvider: (() -> CoroutineContext) -> CoroutineContext = { it() }

    @JvmStatic
    val main = mainProvider { Dispatchers.Main }

    @JvmStatic
    var ioProvider: (() -> CoroutineContext) -> CoroutineContext = { it() }

    @JvmStatic
    val io = ioProvider { Dispatchers.IO }

    fun resetDefaults() {
        defaultProvider = { it() }
        mainProvider = { it() }
        ioProvider = { it() }
    }
}