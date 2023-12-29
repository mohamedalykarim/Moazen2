package mohalim.islamic.alarm.alert.moazen.core.model

import java.util.Calendar

data class NextPray(val calendar: Calendar, val azanType: String, val millisecondDifference: Long)
