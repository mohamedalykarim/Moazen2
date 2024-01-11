package mohalim.islamic.alarm.alert.moazen.core.utils

import android.content.Context
import mohalim.islamic.alarm.alert.moazen.core.model.City
import org.json.JSONObject

class Utils {
    companion object{
        fun getCitiesFromAssets(context: Context): MutableList<City> {
            val cities : MutableList<City>  = ArrayList()
            context.resources.assets.open("cities.json").bufferedReader().use {
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
            context.resources.assets.open("cities.json").bufferedReader().use {
                val jsonString = it.readText()
                val jsonObject = JSONObject(jsonString)
                val citiesJsonArray = jsonObject.getJSONArray("Cities")
                for (i in 0 until citiesJsonArray.length()) {
                    val item = citiesJsonArray.getJSONObject(i)
                    if (item.getString("name") == currentCity)
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
        }
    }
}
