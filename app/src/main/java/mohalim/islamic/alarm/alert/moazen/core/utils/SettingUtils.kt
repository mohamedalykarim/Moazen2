package mohalim.islamic.alarm.alert.moazen.core.utils

import mohalim.islamic.alarm.alert.moazen.R
import org.intellij.lang.annotations.Language

class SettingUtils {
    companion object{
        fun getAzanPerformerNameByRawId(rawId: Int): String {
            var name = ""
            when(rawId){
                R.raw.hamdoon_hamady->{ name = "Hamdoon Hamady" }
                R.raw.abdulrahman_mossad->{ name = "Abdulrahman Mossad" }
                R.raw.elharam_elmekky->{ name = "Elharam Elmekky" }
                R.raw.hafiz_ahmed->{ name = "Hafiz Ahmed" }
                R.raw.idris_aslami->{ name = "Idris Aslami" }
                R.raw.islam_sobhy->{ name = "Islam Sobhy" }
                R.raw.naser_elkatamy->{ name = "Naser Elkatamy" }

            }

            return name
        }
    }
}