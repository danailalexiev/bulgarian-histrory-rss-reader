package bg.dalexiev.bgHistroryRss.data.db.converter

import androidx.room.TypeConverter
import java.util.*

object CalendarTypeConverter {

    @JvmStatic
    @TypeConverter
    fun calendarToLong(value: Calendar): Long = value.timeInMillis

    @JvmStatic
    @TypeConverter
    fun longToCalendar(value: Long): Calendar = Calendar.getInstance().apply { timeInMillis = value }
}