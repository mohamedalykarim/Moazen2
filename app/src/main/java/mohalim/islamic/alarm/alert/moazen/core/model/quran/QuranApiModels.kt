package mohalim.islamic.alarm.alert.moazen.core.model.quran

import com.google.gson.annotations.SerializedName

data class QuranApiResponse<T>(
    val code: Int,
    val status: String,
    val data: T
)

data class SurahApi(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val numberOfAyahs: Int
)

data class PageApi(
    val number: Int,
    val ayahs: List<AyahApi>,
    val surahs: Map<String, SurahApiBrief>
)

data class AyahApi(
    val number: Int,
    val text: String,
    val surah: SurahApiBrief,
    val numberInSurah: Int,
    val juz: Int,
    val manzil: Int,
    val page: Int,
    val ruku: Int,
    val hizbQuarter: Int,
    val sajda: Any?
)

data class SurahApiBrief(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String
)
