package mohalim.islamic.alarm.alert.moazen.core.utils

import android.content.Context
import mohalim.islamic.alarm.alert.moazen.R
import java.io.File

class HadithUtils {
    companion object{
        fun checkIfFileExists(context : Context, rowaa: String): Boolean {
            val fileName = getFileName(context, rowaa)
            return File(context.filesDir, fileName).exists()
        }

        private fun getFileName(context: Context, rowaa: String): String{
            var fileName = ""
            when(rowaa){
                context.getString(R.string.abi_daud) -> fileName = "abi_daud"
                context.getString(R.string.ahmed) -> fileName = "ahmed"
                context.getString(R.string.bukhari) -> fileName = "bukhari"
                context.getString(R.string.darimi) -> fileName = "darimi"
                context.getString(R.string.ibn_maja) -> fileName = "ibn_maja"
                context.getString(R.string.malik) -> fileName = "malik"
                context.getString(R.string.muslim) -> fileName = "muslim"
                context.getString(R.string.nasai) -> fileName = "nasai"
                context.getString(R.string.trmizi) -> fileName = "trmizi"
            }

            return fileName + ".json"
        }

    }
}