package bg.dalexiev.bgHistroryRss.data.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<in Entity> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entities: List<Entity>): List<Long>

}