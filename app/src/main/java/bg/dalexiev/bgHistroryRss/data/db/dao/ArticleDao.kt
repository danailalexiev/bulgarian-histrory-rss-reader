package bg.dalexiev.bgHistroryRss.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import bg.dalexiev.bgHistroryRss.core.distinct
import bg.dalexiev.bgHistroryRss.data.entity.Article
import bg.dalexiev.bgHistroryRss.data.entity.ArticlePreview

@Dao
abstract class ArticleDao : BaseDao<Article> {

    fun loadArticlePreviews() = loadArticlePreviewsInternal().distinct()

    @Query("select guid, title, image_url, link, publish_date, is_read, is_favourite from articles order by publish_date desc")
    protected abstract fun loadArticlePreviewsInternal(): LiveData<List<ArticlePreview>>

    fun loadArticlePreviewsByCategory(categoryId: Long) = loadArticlePreviewsByCategoryInternal(categoryId).distinct()

    @Query("select guid, title, image_url, link, publish_date, is_read, is_favourite from articles a join articles_to_categories ac on a.guid = ac.article_id join categories c on ac.category_id = c.id where c.id = :categoryId order by publish_date desc")
    protected abstract fun loadArticlePreviewsByCategoryInternal(categoryId: Long): LiveData<List<ArticlePreview>>

    @Query("update articles set is_favourite = not is_favourite where guid = :articleGuid")
    abstract fun toggleArticleIsFavourite(articleGuid: String)

    fun loadArticleByGuid(articleGuid: String) = loadArticleByGuidInternal(articleGuid).distinct()

    @Query("select * from articles where guid = :articleGuid")
    protected abstract fun loadArticleByGuidInternal(articleGuid: String): LiveData<Article>
}