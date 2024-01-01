package mohalim.islamic.alarm.alert.moazen.core.utils

import android.content.Context
import android.util.Log
import mohalim.islamic.alarm.alert.moazen.core.model.NextPray
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

class TimesUtils {

    companion object{
        fun getPraysForToday(context: Context, cityName: String, daysDifference: Int): JSONArray {
            var jsonArray = JSONArray();
            context.resources.assets.open("$cityName.json").bufferedReader().use {
                val jsonString = it.readText()
                val jsonObject = JSONObject(jsonString)
                val dateJsonArray = jsonObject.getJSONArray(cityName)

                val calendar = Calendar.getInstance()
                if (daysDifference != 0){
                    val millisecondsDifference = daysDifference * 24 * 60 * 60 * 1000
                    calendar.timeInMillis = calendar.timeInMillis + millisecondsDifference
                }

                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                val todayString: String = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-" + if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
                Log.d("TAG", "getPraysForToday: todayString "+ todayString)
                for (i in 0 until dateJsonArray.length()) {
                    val item = dateJsonArray.getJSONObject(i)
                    if (item.has(todayString)) {
                        jsonArray =  item.getJSONArray(todayString)
                    }
                }
            }

            return jsonArray

        }

        fun getNextPray(jsonArray : JSONArray, daysDifference: Int): NextPray? {
            var date = ""
            var nextDayDate = ""
            var azanType = ""
            val calendarToday = Calendar.getInstance()
            val calendartommorow = Calendar.getInstance()

            var year = 0
            var monthLong = 0
            var dayLong = 0

            if (daysDifference == 0){
                year = calendarToday.get(Calendar.YEAR)
                monthLong = calendarToday.get(Calendar.MONTH) + 1
                dayLong = calendarToday.get(Calendar.DAY_OF_MONTH)
            }else{
                val millisecondsDifference = daysDifference * 24 * 60 * 60 * 1000
                calendartommorow.timeInMillis = calendartommorow.timeInMillis + millisecondsDifference

                year = calendartommorow.get(Calendar.YEAR)
                monthLong = calendartommorow.get(Calendar.MONTH) + 1
                dayLong = calendartommorow.get(Calendar.DAY_OF_MONTH)

                if (monthLong == 12 && dayLong == 31) year +=1
            }

            var month = ""
            var day = ""
            month = if(monthLong < 10) "0$monthLong" else monthLong.toString()
            day = if(dayLong < 10) "0$dayLong" else dayLong.toString()


            for (i in 0 until jsonArray.length()) {
                date = "$year-$month-${day}T${jsonArray.get(i)}:00"
                Log.d("TAG", "getNextPray1: "+date)
                val dateCalendar = localDateTimeStringToCalender(date)
                if (calendarToday.timeInMillis < dateCalendar.timeInMillis){
                    nextDayDate = "$year-$month-${day}T${jsonArray.get(i)}:00"
                    when (i) {
                        0 -> azanType = "AZAN_TYPE_FAGR"
                        1 -> azanType = "AZAN_TYPE_SHROUQ"
                        2 -> azanType = "AZAN_TYPE_ZOHR"
                        3 -> azanType = "AZAN_TYPE_ASR"
                        4 -> azanType = "AZAN_TYPE_GHROUB"
                        5 -> azanType = "AZAN_TYPE_MAGHREB"
                        6 -> azanType = "AZAN_TYPE_ESHA"
                    }
                    break
                }
            }

            if (nextDayDate == ""){
                return null
            }


            val nextCalendar =  localDateTimeStringToCalender(nextDayDate)

            val millisecondDifference = nextCalendar.timeInMillis - calendarToday.timeInMillis

            return NextPray(nextCalendar, azanType, millisecondDifference)
        }

        fun localDateTimeStringToCalender(input: String): Calendar {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val localDateTime =  LocalDateTime.parse(input, formatter)
            val millis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            return calendar
        }

        fun convertMillisecondsToTime(totalMilliseconds: Long): Triple<String, String, String> {
            val hours = totalMilliseconds / (1000 * 60 * 60)
            val remainingMilliseconds = totalMilliseconds % (1000 * 60 * 60)
            val minutes = remainingMilliseconds / (1000 * 60)
            val seconds = (remainingMilliseconds % (1000 * 60)) / 1000
            var hoursString = ""
            var minutesString = ""
            var secondsString = ""
            hoursString = if (hours < 10){ "0$hours" }else{ hours.toString() }
            minutesString = if (minutes < 10){ "0$minutes" }else{ minutes.toString() }
            secondsString = if (seconds < 10){ "0$seconds" }else{ seconds.toString() }
            return Triple(hoursString, minutesString, secondsString)
        }

        fun getPrayersInTimeFormat(prayers : JSONArray): MutableList<String> {
            val calendar = Calendar.getInstance()
            val list : MutableList<String> = ArrayList()
            for (i in 0 until prayers.length()) {
                val item = prayers.get(i)
                val splitedValue = item.toString().split(":")
                var hour = splitedValue[0].toInt()
                val minute = splitedValue[1].toInt()

                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                list.add(formatter.format(calendar.time).replace("am", "AM").replace("pm", "PM"))
            }

            return list

        }


        fun getDate(calendar: Calendar): String {
            val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            return formatter.format(calendar.time)
        }

        fun getHigriDate(hijrahDate: HijrahDate): String {
            var date  = ""
            date = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.getDefault()).format(hijrahDate)
            return date
        }

    }
}