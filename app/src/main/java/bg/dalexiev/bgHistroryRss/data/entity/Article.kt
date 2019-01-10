package bg.dalexiev.bgHistroryRss.data.entity

import java.util.*

data class Article(
    val guid: String,
    val title: String,
    val link: String,
    val publishDate: Calendar,
    val creator: String,
    val imageUrl:String,
    val description: String,
    val categories: List<String>,
    val isRead: Boolean = false,
    val isFavourite: Boolean = false
)