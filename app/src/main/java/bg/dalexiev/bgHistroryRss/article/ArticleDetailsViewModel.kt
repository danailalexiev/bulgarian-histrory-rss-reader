package bg.dalexiev.bgHistroryRss.article

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bg.dalexiev.bgHistroryRss.core.BaseViewModel
import bg.dalexiev.bgHistroryRss.core.CoroutineDispatchers
import bg.dalexiev.bgHistroryRss.core.Event
import bg.dalexiev.bgHistroryRss.core.State
import bg.dalexiev.bgHistroryRss.data.entity.Article
import bg.dalexiev.bgHistroryRss.data.repository.ArticleRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleDetailsViewModel(application: Application, private val articleRepo: ArticleRepository) :
    BaseViewModel(application) {

    private val _article = MutableLiveData<State<Article>>()
    val article: LiveData<State<Article>>
        get() = _article

    private val _imageLoaded = MutableLiveData<Event<Unit>>()
    val imageLoaded: LiveData<Event<Unit>>
        get() = _imageLoaded

    fun init(articleGuid: String) {
        scope.launch {
            _article.value = State.loading()
            try {
                withContext(CoroutineDispatchers.io) {
                    articleRepo.loadArticleByGuid(articleGuid)
                }.also { _article.value = State.success(it) }
            } catch (t: Throwable) {
                _article.value = State.failure(t)
            }
        }
    }

    fun onArticleImageLoaded() {
        _imageLoaded.value = Event(Unit)
    }
}