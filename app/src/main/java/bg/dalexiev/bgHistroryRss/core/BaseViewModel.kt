package bg.dalexiev.bgHistroryRss.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val job = SupervisorJob()
    protected val scope = CoroutineScope(CoroutineDispatchers.main + job)

    override fun onCleared() {
        job.cancelChildren()
    }
}