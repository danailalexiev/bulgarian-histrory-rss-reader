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
                .items.map { rssFeedItem ->
                Article(
                    guid = rssFeedItem.guid,
                    title = rssFeedItem.title,
                    link = rssFeedItem.link,
                    publishDate = rssFeedItem.publishDate,
                    creator = rssFeedItem.creator,
                    description = rssFeedItem.description
                )
            }
        }
    }
}