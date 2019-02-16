package bg.dalexiev.bgHistroryRss.article

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bg.dalexiev.bgHistroryRss.R
import bg.dalexiev.bgHistroryRss.core.BaseViewModel
import bg.dalexiev.bgHistroryRss.core.CoroutineDispatchers
import bg.dalexiev.bgHistroryRss.core.Event
import bg.dalexiev.bgHistroryRss.data.entity.ArticlePreview
import bg.dalexiev.bgHistroryRss.data.repository.ArticleRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleListViewModel(application: Application, private val articleRepo: ArticleRepository) :
    BaseViewModel(application) {

    val articles: LiveData<List<ArticlePreview>> = articleRepo.loadArticlePreviews(null)

    private val _sharedArticle = MutableLiveData<Event<String>>()
    val sharedArticle: LiveData<Event<String>>
        get() = _sharedArticle

    private val _selectedArticle = MutableLiveData<Event<Pair<Int, ArticlePreview>>>()
    val selectedArticle: LiveData<Event<Pair<Int, ArticlePreview>>>
        get() = _selectedArticle

    private val _error = MutableLiveData<Event<Int>>()
    val error: LiveData<Event<Int>>
        get() = _error

    fun sync() {
        scope.launch {
            try {
                withContext(CoroutineDispatchers.io) { articleRepo.sync() }
            } catch (e: Exception) {
                _error.value = Event(R.string.sync_error_message)
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
                _error.value = Event(R.string.update_error_message)
            }
        }
    }

    fun onArticleClicked(position: Int, article: ArticlePreview) {
        _selectedArticle.value = Event(position to article)
    }
}