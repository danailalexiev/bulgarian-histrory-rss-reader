package bg.dalexiev.bgHistroryRss.article

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import bg.dalexiev.bgHistroryRss.core.BaseViewModel
import bg.dalexiev.bgHistroryRss.core.CoroutineDispatchers
import bg.dalexiev.bgHistroryRss.core.Event
import bg.dalexiev.bgHistroryRss.core.State
import bg.dalexiev.bgHistroryRss.data.repository.ArticleRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleDetailsViewModel(application: Application, private val articleRepo: ArticleRepository) :
    BaseViewModel(application) {

    private val articleGuid = MutableLiveData<String>()
    val article = (Transformations.switchMap(articleGuid) { articleRepo.loadArticleByGuid(it) })!!

    private val _imageLoaded = MutableLiveData<Event<Unit>>()
    val imageLoaded: LiveData<Event<Unit>>
        get() = _imageLoaded

    fun init(guid: String) {
        articleGuid.value = guid
    }

    fun onArticleImageLoaded() {
        _imageLoaded.value = Event(Unit)
    }
}