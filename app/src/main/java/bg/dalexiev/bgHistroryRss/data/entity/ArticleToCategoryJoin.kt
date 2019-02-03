package bg.dalexiev.bgHistroryRss.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "articles_to_categories",
    primaryKeys = ["article_id", "category_id"],
    foreignKeys = [
        ForeignKey(
            entity = Article::class,
            parentColumns = arrayOf("guid"),
            childColumns = arrayOf("article_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"),
            onDelete = ForeignKey.NO_ACTION
        )],
    indices = [Index(value = arrayOf("category_id"))]
)
class ArticleToCategoryJoin(

    @ColumnInfo(name = "article_id", typeAffinity = ColumnInfo.TEXT)
    val articleId: String,

    @ColumnInfo(name = "category_id", typeAffinity = ColumnInfo.INTEGER)
    val categoryId: Long
)