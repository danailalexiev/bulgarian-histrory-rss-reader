package bg.dalexiev.bgHistroryRss.article

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bg.dalexiev.bgHistroryRss.core.BaseViewModel
import bg.dalexiev.bgHistroryRss.core.CoroutineDispatchers
import bg.dalexiev.bgHistroryRss.core.Event
import bg.dalexiev.bgHistroryRss.core.State
import bg.dalexiev.bgHistroryRss.data.entity.ArticlePreview
import bg.dalexiev.bgHistroryRss.data.repository.ArticleRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleListViewModel(application: Application, private val articleRepo: ArticleRepository) :
    BaseViewModel(application) {

    private val _articles = MutableLiveData<State<List<ArticlePreview>>>()

    val articles: LiveData<State<List<ArticlePreview>>>
        get() = _articles

    private val _sharedArticle = MutableLiveData<Event<Intent>>()

    val sharedArticle: LiveData<Event<Intent>>
        get() = _sharedArticle

    fun loadArticles() = scope.launch {
        _articles.value = State.loading()
        try {
            withContext(CoroutineDispatchers.io) { articleRepo.loadArticlePreviews(null) }
                .also { _articles.value = State.success(it) }
        } catch (e: Exception) {
            _articles.value = State.failure(e)
        }
    }

    fun sync() = scope.launch {
        _articles.value = State.loading()
        try {
            withContext(CoroutineDispatchers.io) {
                articleRepo.sync();
                return@withContext articleRepo.loadArticlePreviews(null)
            }.also { _articles.value = State.success(it) }
        } catch (e: Exception) {
            _articles.value = State.failure(e)
        }
    }

    fun onShareArticleClicked(activity: Activity, article: ArticlePreview): Unit =
        ShareCompat.IntentBuilder.from(activity)
            .setType("texp/plain")
            .setText(article.link)
            .intent
            .run { _sharedArticle.value = Event(this) }
}