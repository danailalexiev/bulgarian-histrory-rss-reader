package bg.dalexiev.bgHistroryRss.data.entity

import java.util.*

data class Article(
    val guid: String,
    val title: String,
    val link: String,
    val publishDate: Calendar,
    val creator: String,
    val description: String,
    val isRead: Boolean = false,
    val isFavourite: Boolean = false
)