package bg.dalexiev.bgHistroryRss.data.rss

import bg.dalexiev.bgHistroryRss.core.Provider
import java.net.HttpURLConnection
import java.net.URL

private const val FEED_URL = "https://bulgarianhistory.org/feed/"


interface RssService {

    fun loadFeed(): RssFeed

    companion object : Provider<RssService>() {

        override fun create(): RssService {
            return RssServiceImpl()
        }

    }

    private class RssServiceImpl : RssService {

        private val parser: RssParser = RssParser()

        override fun loadFeed(): RssFeed {
            var connection: HttpURLConnection? = null
            try {
                connection = URL(FEED_URL).openConnection().apply {
                    readTimeout = 8000
                    connectTimeout = 2000
                } as HttpURLConnection
                return connection.inputStream.buffered().use { parser.parseFeed(it) }
            } finally {
                connection?.disconnect()
            }
        }
    }
}