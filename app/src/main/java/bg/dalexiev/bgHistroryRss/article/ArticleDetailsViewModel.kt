package bg.dalexiev.bgHistroryRss.article

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import bg.dalexiev.bgHistroryRss.core.BaseViewModel
import bg.dalexiev.bgHistroryRss.core.Event
import bg.dalexiev.bgHistroryRss.data.repository.ArticleRepository

class ArticleDetailsViewModel(application: Application, private val articleRepo: ArticleRepository) :
    BaseViewModel(application) {

    private val articleGuid = MutableLiveData<String>()
    val article = (Transformations.switchMap(articleGuid) { articleRepo.loadArticleByGuid(it) })!!

    private val _imageLoaded = MutableLiveData<Event<Unit>>()
    val imageLoaded: LiveData<Event<Unit>>
        get() = _imageLoaded

    private val _sharedLink = MutableLiveData<Event<String>>()
    val sharedLink: LiveData<Event<String>>
        get() = _sharedLink

    fun init(guid: String) {
        articleGuid.value = guid
    }

    fun onArticleImageLoaded() {
        _imageLoaded.value = Event(Unit)
    }

    fun onShareLinkClicked() {
        _sharedLink.value = article.value?.let { Event(it.link) }
    }
}