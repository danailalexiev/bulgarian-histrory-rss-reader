package bg.dalexiev.bgHistroryRss.data.repository

import android.content.Context
import bg.dalexiev.bgHistroryRss.core.Provider
import bg.dalexiev.bgHistroryRss.data.db.ArticleDatabase
import bg.dalexiev.bgHistroryRss.data.db.dao.ArticleDao
import bg.dalexiev.bgHistroryRss.data.db.dao.ArticleToCategoryJoinDao
import bg.dalexiev.bgHistroryRss.data.db.dao.CategoryDao
import bg.dalexiev.bgHistroryRss.data.entity.Article
import bg.dalexiev.bgHistroryRss.data.entity.ArticlePreview
import bg.dalexiev.bgHistroryRss.data.entity.ArticleToCategoryJoin
import bg.dalexiev.bgHistroryRss.data.entity.Category
import bg.dalexiev.bgHistroryRss.data.rss.RssFeedItem
import bg.dalexiev.bgHistroryRss.data.rss.RssService

interface ArticleRepository {

    fun loadArticlePreviews(category: Category?): List<ArticlePreview>

    fun sync()

    companion object : Provider<ArticleRepository, Context>() {

        override fun create(param: Context): ArticleRepository {
            val db = ArticleDatabase.getInstance(param)
            return ArticleRepositoryImpl(
                RssService.get(Unit),
                db.getArticleDao(),
                db.getCategoryDao(),
                db.getArticleToCategoryJoinDao(),
                db
            )
        }

    }

    private class ArticleRepositoryImpl(
        private val service: RssService,
        private val articleDao: ArticleDao,
        private val categoryDao: CategoryDao,
        private val articleToCategoryJoinDao: ArticleToCategoryJoinDao,
        private val db: ArticleDatabase
    ) : ArticleRepository {

        override fun sync() {
            decomposition(service.loadFeed().items).also { persist(it) }
        }

        private fun decomposition(items: List<RssFeedItem>): Triple<List<Category>, List<Article>, Map<String, List<String>>> {
            val categories = mutableListOf<Category>()
            val articles = mutableListOf<Article>()
            val categoriesForArticle = mutableMapOf<String, List<String>>()
            items.forEach { item ->
                categories.addAll(item.categories.map { Category(name = it) })
                articles.add(
                    Article(
                        guid = item.guid,
                        title = item.title,
                        link = item.link,
                        publishDate = item.publishDate,
                        creator = item.creator,
                        imageUrl = item.imageUrl,
                        description = item.description
                    )
                )
                categoriesForArticle.plusAssign(item.guid to item.categories)
            }

            return Triple(categories, articles, categoriesForArticle)
        }

        private fun persist(data: Triple<List<Category>, List<Article>, Map<String, List<String>>>) =
            db.runInTransaction {
                categoryDao.insert(data.first)
                articleDao.insert(data.second)
                data.third.flatMap { articleToCategories ->
                    categoryDao.getIdByName(articleToCategories.value)
                        .map { ArticleToCategoryJoin(articleId = articleToCategories.key, categoryId = it) }
                }.also { articleToCategoryJoinDao.insert(it) }
            }

        override fun loadArticlePreviews(category: Category?): List<ArticlePreview> =
            category?.let { articleDao.loadArticlePreviewsByCategory(it.id!!) } ?: articleDao.loadArticlePreviews()

    }
}