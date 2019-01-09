package bg.dalexiev.bgHistroryRss.data.rss

data class RssFeed(
    val title: String,
    val link: String,
    val description: String,
    val items: List<RssFeedItem>)