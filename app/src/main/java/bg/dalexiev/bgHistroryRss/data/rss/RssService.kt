package bg.dalexiev.bgHistroryRss.data.rss

import android.util.Log
import bg.dalexiev.bgHistroryRss.core.Provider
import java.net.HttpURLConnection
import java.net.URL

private const val FEED_URL = "https://bulgarianhistory.org/feed/"


interface RssService {

    fun loadFeed(): RssFeed

    companion object : Provider<RssService, Unit>() {

        override fun create(param: Unit): RssService {
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
            } catch (e: Throwable) {
                Log.e("RssService", "Error while loading RSS feed", e)
                throw e
            } finally {
                connection?.disconnect()
            }
        }
    }
}