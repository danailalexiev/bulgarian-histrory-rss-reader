package bg.dalexiev.bgHistroryRss.data.db.converter

import androidx.room.TypeConverter

object BooleanTypeConverter {

    @JvmStatic
    @TypeConverter
    fun booleanToInt(value: Boolean?): Int? = when (value) {
        false -> 0
        true -> 1
        else -> null
    }

    @JvmStatic
    @TypeConverter
    fun intToBoolean(value: Int?): Boolean? = when (value) {
        0 -> false
        1 -> true
        else -> null
    }
}