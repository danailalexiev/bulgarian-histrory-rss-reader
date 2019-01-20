package bg.dalexiev.bgHistroryRss.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import bg.dalexiev.bgHistroryRss.data.entity.Article
import bg.dalexiev.bgHistroryRss.data.entity.ArticlePreview

@Dao
abstract class ArticleDao : BaseDao<Article> {

    @Query("select guid, title, image_url, link, publish_date, is_read, is_favourite from articles order by publish_date desc")
    abstract fun loadArticlePreviews(): List<ArticlePreview>

    @Query("select guid, title, image_url, link, publish_date, is_read, is_favourite from articles a join articles_to_categories ac on a.guid = ac.article_id join categories c on ac.category_id = c.id where c.id = :categoryId order by publish_date desc")
    abstract fun loadArticlePreviewsByCategory(categoryId: Long): List<ArticlePreview>
}