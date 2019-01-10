package bg.dalexiev.bgHistroryRss.data.rss

import java.util.*

data class RssFeedItem(
    val guid: String,
    val title: String,
    val link: String,
    val publishDate: Calendar,
    val creator: String,
    val imageUrl: String,
    val description: String,
    val categories: List<String>
)