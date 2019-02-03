package bg.dalexiev.bgHistroryRss.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import bg.dalexiev.bgHistroryRss.data.db.converter.BooleanTypeConverter
import bg.dalexiev.bgHistroryRss.data.db.converter.CalendarTypeConverter
import java.util.*

@Entity(tableName = "articles")
data class Article(

    @PrimaryKey
    @ColumnInfo(name = "guid", typeAffinity = ColumnInfo.TEXT)
    val guid: String,

    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT)
    val title: String,

    @ColumnInfo(name = "link", typeAffinity = ColumnInfo.TEXT)
    val link: String,

    @ColumnInfo(name = "publish_date")
    @field:TypeConverters(CalendarTypeConverter::class)
    val publishDate: Calendar,

    @ColumnInfo(name = "creator", typeAffinity = ColumnInfo.TEXT)
    val creator: String,

    @ColumnInfo(name = "image_url", typeAffinity = ColumnInfo.TEXT)
    val imageUrl: String,

    @ColumnInfo(name = "description", typeAffinity = ColumnInfo.TEXT)
    val description: String,

    @ColumnInfo(name = "is_read")
    @field:TypeConverters(BooleanTypeConverter::class)
    val isRead: Boolean = false,

    @ColumnInfo(name = "is_favourite")
    @field:TypeConverters(BooleanTypeConverter::class)
    val isFavourite: Boolean = false
)