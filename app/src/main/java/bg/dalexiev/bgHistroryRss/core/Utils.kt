package bg.dalexiev.bgHistroryRss.core

import android.app.Activity
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import bg.dalexiev.bgHistroryRss.App

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

inline fun <reified T : ViewModel> Fragment.getViewModel() =
    ViewModelProviders.of(this, (context!!.applicationContext as App).viewModelFactory).get(T::class.java)

fun Fragment.shareText(text: String) = with(createShareIntent(text, activity!!)) {
    resolveActivity(activity!!.packageManager)?.let { startActivity(this) }
}

private fun createShareIntent(link: String, activity: Activity) = ShareCompat.IntentBuilder.from(activity)
    .setType("texp/plain")
    .setText(link)
    .intent