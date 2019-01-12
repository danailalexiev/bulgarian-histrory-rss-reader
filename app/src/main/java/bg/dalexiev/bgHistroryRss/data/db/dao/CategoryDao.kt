package bg.dalexiev.bgHistroryRss.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import bg.dalexiev.bgHistroryRss.data.entity.Category

@Dao
abstract class CategoryDao : BaseDao<Category> {

    @Query("select id from categories where name in (:names)")
    abstract fun getIdByName(names: List<String>): List<Long>

}