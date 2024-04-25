package mohalim.islamic.alarm.alert.moazen.core.model

data class Surah(
    var place : String,
    var type : String,
    val verseCount : Int,
    val revelationOrder : Int,
    val rukus : Int,
    val title : String,
    val titleAr : String,
    val titleEn : String,
    val index : String,
    val pages : Int,
    val page : Int,
    val start : Int,
    val juz : MutableList<Juz>

)
