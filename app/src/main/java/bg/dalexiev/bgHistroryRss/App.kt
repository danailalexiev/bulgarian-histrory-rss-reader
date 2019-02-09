package bg.dalexiev.bgHistroryRss

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bg.dalexiev.bgHistroryRss.article.ArticleDetailsViewModel
import bg.dalexiev.bgHistroryRss.article.ArticleListViewModel
import bg.dalexiev.bgHistroryRss.data.repository.ArticleRepository

class App : Application() {

    @Suppress("UNCHECKED_CAST")
    val viewModelFactory = object : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when (modelClass) {
                ArticleListViewModel::class.java -> ArticleListViewModel(this@App, ArticleRepository.get(this@App)) as T
                ArticleDetailsViewModel::class.java -> ArticleDetailsViewModel(this@App, ArticleRepository.get(this@App)) as T
                else -> throw IllegalArgumentException("Unknown view model class $modelClass")
            }
        }

    }

}