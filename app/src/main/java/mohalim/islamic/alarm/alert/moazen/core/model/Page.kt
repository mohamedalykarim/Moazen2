package mohalim.islamic.alarm.alert.moazen.core.model

data class Page(
    var pageNumber : Int,
    val startIndex : Int,
    val startVerse : Int,
    val startSurahName : String,
    val startSurahArName : String,
    val endIndex : Int,
    val endVerse : Int,
    val endSurahName : String,
    val endSurahArName : String
)
