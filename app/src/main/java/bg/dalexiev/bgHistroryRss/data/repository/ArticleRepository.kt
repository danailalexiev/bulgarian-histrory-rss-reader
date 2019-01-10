package bg.dalexiev.bgHistroryRss.data.repository

import bg.dalexiev.bgHistroryRss.core.Provider
import bg.dalexiev.bgHistroryRss.data.entity.Article
import bg.dalexiev.bgHistroryRss.data.rss.RssService

interface ArticleRepository {

    fun loadArticles(): List<Article>

    companion object: Provider<ArticleRepository>() {

        override fun create(): ArticleRepository {
            return ArticleRepositoryImpl(RssService.get())
        }

    }

    private class ArticleRepositoryImpl(val service: RssService) : ArticleRepository {

        override fun loadArticles(): List<Article> {
            return service.loadFeed()
                .items.map { it ->
                Article(
                    guid = it.guid,
                    title = it.title,
                    link = it.link,
                    publishDate = it.publishDate,
                    creator = it.creator,
                    imageUrl = it.imageUrl,
                    description = it.description,
                    categories = it.categories
                )
            }
        }
    }
}