package bg.dalexiev.bgHistroryRss.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bg.dalexiev.bgHistroryRss.data.db.dao.ArticleDao
import bg.dalexiev.bgHistroryRss.data.db.dao.ArticleToCategoryJoinDao
import bg.dalexiev.bgHistroryRss.data.db.dao.CategoryDao
import bg.dalexiev.bgHistroryRss.data.entity.Article
import bg.dalexiev.bgHistroryRss.data.entity.ArticleToCategoryJoin
import bg.dalexiev.bgHistroryRss.data.entity.Category

@Database(
    entities = [Article::class, Category::class, ArticleToCategoryJoin::class],
    version = 1,
    exportSchema = false
)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    abstract fun getCategoryDao(): CategoryDao

    abstract fun getArticleToCategoryJoinDao(): ArticleToCategoryJoinDao

    companion object {

        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        fun getInstance(context: Context): ArticleDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }

            }

        private fun buildDatabase(context: Context): ArticleDatabase =
            Room.databaseBuilder(context.applicationContext, ArticleDatabase::class.java, "articles.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}