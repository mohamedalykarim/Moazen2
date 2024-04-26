package mohalim.islamic.alarm.alert.moazen.core.utils

import android.content.Context
import mohalim.islamic.alarm.alert.moazen.core.model.City
import mohalim.islamic.alarm.alert.moazen.core.model.Juz
import mohalim.islamic.alarm.alert.moazen.core.model.Page
import mohalim.islamic.alarm.alert.moazen.core.model.Surah
import org.json.JSONArray
import org.json.JSONObject

class Utils {
    companion object{
        fun getCitiesFromAssets(context: Context): MutableList<City> {
            val cities : MutableList<City>  = ArrayList()
            context.resources.assets.open("data_cities.json").bufferedReader().use {
                val jsonString = it.readText()
                val jsonObject = JSONObject(jsonString)
                val citiesJsonArray = jsonObject.getJSONArray("Cities")
                for (i in 0 until citiesJsonArray.length()) {
                    val item = citiesJsonArray.getJSONObject(i)
                    val city = City(
                        item.getString("name"),
                        item.getString("en_name"),
                        item.getString("ar_name"),
                        item.getString("country"),
                        item.getString("ar_country")
                    )

                    cities.add(city)
                }
            }

            return cities

        }

        fun getCurrentCity(context: Context, currentCity: String): City {
            var city = City("","","","","")

            try {
                context.resources.assets.open("data_cities.json").bufferedReader().use {
                    val jsonString = it.readText()
                    val jsonObject = JSONObject(jsonString)
                    val citiesJsonArray = jsonObject.getJSONArray("Cities")
                    for (i in 0 until citiesJsonArray.length()) {
                        val item = citiesJsonArray.getJSONObject(i)
                        if (item!!.getString("name") == currentCity)
                            city = City(
                                item.getString("name"),
                                item.getString("en_name"),
                                item.getString("ar_name"),
                                item.getString("country"),
                                item.getString("ar_country")
                            )
                    }
                }

                return city
            }catch (e : Exception){
                return city
            }
        }

        fun getAllSurahMetaData(context: Context): MutableList<Surah> {
            val allSurah : MutableList<Surah> = ArrayList()

            try {
                context.resources.assets.open("data_surah.json").bufferedReader().use {
                    val jsonString = it.readText()
                    val jsonArray = JSONArray(jsonString)

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val juzs = item.getJSONArray("juz")

                        val allJuz : MutableList<Juz> = ArrayList()

                        for (m in 0 until juzs.length()) {
                            val juz = juzs.getJSONObject(m)
                            val verseStartEnd = juz.getJSONObject("verse")

                            val newJuz = Juz(
                                juz.getString("index").toInt(),
                                verseStartEnd.getString("start").replace("verse_","").toInt(),
                                verseStartEnd.getString("end").replace("verse_","").toInt(),
                            )

                            allJuz.add(newJuz)
                        }

                        val surah = Surah(
                            place = item.getString("place"),
                            type = item.getString("type"),
                            verseCount = item.getInt("count"),
                            revelationOrder = item.getInt("revelationOrder"),
                            rukus = item.getInt("rukus"),
                            title = item.getString("title"),
                            titleAr = item.getString("titleAr"),
                            titleEn = item.getString("titleEn"),
                            index = item.getString("index"),
                            pages = item.getString("pages").toInt(),
                            page = item.getString("page").toInt(),
                            start = item.getInt("start"),
                            juz = allJuz
                        )

                        allSurah.add(surah)
                    }

                    return allSurah
                }
            }catch (exception : Exception){
                return allSurah
            }
        }

        fun getPageData(context: Context, pageNumber: Int) : Page{
            var page = Page(
                1,
                1,
                1,
                "Al-Fatiha",
                "الفاتحة",
                1,
                7,
                "Al-Fatiha",
                "الفاتحة",

                )
            var pageNumberString = pageNumber.toString()
            when(pageNumberString.length){
                1 -> pageNumberString = "00$pageNumberString"
                2 -> pageNumberString = "0$pageNumberString"
                3 -> {}
            }
            try {
                context.resources.assets.open("data_page.json").bufferedReader().use {
                    val jsonString = it.readText()
                    val jsonArray = JSONArray(jsonString)
                    for (i in 0 until jsonArray.length()) {
                        val index = jsonArray.getJSONObject(i).getString("index")
                        if (pageNumberString == index){
                            page = Page(
                                pageNumber = pageNumber,
                                startIndex = jsonArray.getJSONObject(i).getJSONObject("start").getString("index").toInt(),
                                startVerse = jsonArray.getJSONObject(i).getJSONObject("start").getString("index").replace("verse_","").toInt(),
                                startSurahName = jsonArray.getJSONObject(i).getJSONObject("start").getString("name"),
                                startSurahArName = jsonArray.getJSONObject(i).getJSONObject("start").getString("nameAr"),
                                endIndex = jsonArray.getJSONObject(i).getJSONObject("end").getString("index").toInt(),
                                endVerse = jsonArray.getJSONObject(i).getJSONObject("end").getString("index").replace("verse_","").toInt(),
                                endSurahName = jsonArray.getJSONObject(i).getJSONObject("end").getString("name"),
                                endSurahArName = jsonArray.getJSONObject(i).getJSONObject("end").getString("nameAr")
                            )

                            break
                        }
                    }

                }

            }catch (exception : Exception){

            }

            return page
        }

        fun dipTopx(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * px to dp
         */
        fun pxTodip(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }
    }
}
