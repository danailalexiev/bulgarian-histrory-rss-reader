package bg.dalexiev.bgHistroryRss.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = arrayOf("name"), unique = true)]
)
data class Category(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Long? = null,

    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    val name: String
)