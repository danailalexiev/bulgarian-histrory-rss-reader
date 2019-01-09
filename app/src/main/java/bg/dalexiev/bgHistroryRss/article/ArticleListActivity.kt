package bg.dalexiev.bgHistroryRss.article

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import bg.dalexiev.bgHistroryRss.data.repository.ArticleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ArticleListActivity : AppCompatActivity() {

    val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        scope.launch { ArticleRepository.get().loadArticles().forEach { it -> Log.d("TEST", it.title) } }
    }
}