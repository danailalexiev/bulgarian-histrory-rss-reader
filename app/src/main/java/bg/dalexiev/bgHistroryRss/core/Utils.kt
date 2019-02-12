package bg.dalexiev.bgHistroryRss.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.distinct(): LiveData<T> {
    val distinctLiveData = MediatorLiveData<T>()
    distinctLiveData.addSource(this, object : Observer<T> {

        private var isInitialized: Boolean = false;
        private var currentValue: T? = null

        override fun onChanged(t: T) {
            if (!isInitialized) {
                isInitialized = true
                currentValue = t
                distinctLiveData.value = currentValue
                return
            }

            if (((currentValue == null) && (t != null))
                || currentValue != t
            ) {
                currentValue = t;
                distinctLiveData.value = currentValue
            }
        }
    })

    return distinctLiveData;
}