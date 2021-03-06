package bg.dalexiev.bgHistroryRss.data.entity

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import bg.dalexiev.bgHistroryRss.data.db.converter.BooleanTypeConverter
import bg.dalexiev.bgHistroryRss.data.db.converter.CalendarTypeConverter
import java.util.*

data class ArticlePreview(
    @ColumnInfo(name = "guid")
    val guid: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "link")
    val link: String,

    @ColumnInfo(name = "publish_date")
    @field:TypeConverters(CalendarTypeConverter::class)
    val publishDate: Calendar,

    @ColumnInfo(name = "is_read")
    @field:TypeConverters(BooleanTypeConverter::class)
    val isRead: Boolean,

    @ColumnInfo(name = "is_favourite")
    @field:TypeConverters(BooleanTypeConverter::class)
    val isFavourite: Boolean
)