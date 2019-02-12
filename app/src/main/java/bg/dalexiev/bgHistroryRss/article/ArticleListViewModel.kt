package bg.dalexiev.bgHistroryRss.article

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    val articles: LiveData<State<List<ArticlePreview>>> =
        (Transformations.map(articleRepo.loadArticlePreviews(null)) { State.success(it) })!!

    private val _sharedArticle = MutableLiveData<Event<String>>()
    val sharedArticle: LiveData<Event<String>>
        get() = _sharedArticle

    private val _selectedArticle = MutableLiveData<Event<Pair<Int, ArticlePreview>>>()
    val selectedArticle: LiveData<Event<Pair<Int, ArticlePreview>>>
        get() = _selectedArticle


    fun sync() {
        scope.launch {
            try {
                withContext(CoroutineDispatchers.io) {
                    articleRepo.sync();
                }
            } catch (e: Exception) {
            }
        }
    }

    fun onShareArticleClicked(article: ArticlePreview) {
        _sharedArticle.value = Event(article.link)
    }

    fun onToggleArticleClicked(article: ArticlePreview) {
        scope.launch {
            try {
                withContext(CoroutineDispatchers.io) { articleRepo.toggleArticleIsFavourite(article) }
            } catch (e: Exception) {
            }
        }
    }

    fun onArticleClicked(position: Int, article: ArticlePreview) {
        _selectedArticle.value = Event(position to article)
    }
}