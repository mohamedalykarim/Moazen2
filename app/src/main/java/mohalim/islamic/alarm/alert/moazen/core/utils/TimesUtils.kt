package mohalim.islamic.alarm.alert.moazen.core.utils

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class TimesUtils {

    companion object{
        fun getPraysForToday(context: Context, cityName: String, daysDifference: Int): JSONArray {
            var jsonArray : JSONArray = JSONArray();
            context.resources.assets.open("$cityName.json").bufferedReader().use {
                val jsonString = it.readText()
                val jsonObject = JSONObject(jsonString)
                val dateJsonArray = jsonObject.getJSONArray(cityName)

                val calendar = Calendar.getInstance()
                if (daysDifference != 0){
                    val millisecondsDifference = daysDifference * 24 * 60 * 60 * 1000
                    calendar.timeInMillis = calendar.timeInMillis + millisecondsDifference
                }
                val todayString: String = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-" + calendar.get(Calendar.DAY_OF_MONTH)

                for (i in 0 until dateJsonArray.length()) {
                    val item = dateJsonArray.getJSONObject(i)
                    if (item.has(todayString)) {
                        jsonArray =  item.getJSONArray(todayString)
                    }
                }
            }

            return jsonArray

        }

        fun getNextPray(jsonArray : JSONArray): Calendar {
            var date = ""
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            for (i in 0 until jsonArray.length()) {
                date = "$year-$month-${day}T${jsonArray.get(i)}:00"
                val dateCalendar = localDateTimeStringToCalender(date)
                if (calendar.timeInMillis < dateCalendar.timeInMillis){
                    date = "$year-$month-${day}T${jsonArray.get(i)}:00"
                }
            }

            return localDateTimeStringToCalender(date)
        }

        fun localDateTimeStringToCalender(input: String): Calendar {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val localDateTime =  LocalDateTime.parse(input, formatter)
            val millis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            return calendar
        }
    }
}