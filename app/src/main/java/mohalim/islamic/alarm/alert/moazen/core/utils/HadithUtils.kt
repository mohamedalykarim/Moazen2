package mohalim.islamic.alarm.alert.moazen.core.utils

import android.content.Context
import android.util.Log
import mohalim.islamic.alarm.alert.moazen.R
import java.io.File

class HadithUtils {
    companion object{
        fun checkIfFileExists(context : Context, rowaa: String): Boolean {
            val fileName = getFileName(context, rowaa)
            return context.getFileStreamPath(fileName+".json").exists();
        }

        fun getFileURL(context: Context, rawy: String): String {
            var url = ""

            val trmiziUrl = "/uc?export=download&id=1bzQIM6TC196NMX7Fnnbf7ZWej_OgbtiI"
            val nasaiUrl = "/uc?export=download&id=1nW4smqPNuJnQvOzIlazpRvgE6sD_Sr56"
            val muslimUrl = "/uc?export=download&id=1l3KqVI1w6eJufla-Fl66HxwtuVXslWLd"
            val malikUrl = "/uc?export=download&id=10MHcOFlSjMHQgoKHPB0wIcorus7n22kj"
            val ibn_majaUrl = "/uc?export=download&id=1RkwxIDTioFNr7bh9sk3Wzc43fxPPNBUr"
            val darimiUrl = "/uc?export=download&id=1_pGFzwc9tqSrbsry6VTtmTPvvZjo8oW_"
            val bukhariUrl = "/uc?export=download&id=1T6uRtboAm1mu8OJI4qfnw4KQP22yVcKq"
            val ahmedUrl = "/uc?export=download&id=1xMO9Ujgd9A_BmfljJj0a3ZUB4zR6twWU"
            val abiDaudUrl = "/uc?export=download&id=1YKb149jQU9A9MqneJWeAYTs6sYi8Ls8m"

            when(rawy){
                context.getString(R.string.abi_daud) ->  url = abiDaudUrl
                context.getString(R.string.ahmed) -> url = ahmedUrl
                context.getString(R.string.bukhari) -> url = bukhariUrl
                context.getString(R.string.darimi) -> url = darimiUrl
                context.getString(R.string.ibn_maja) -> url = ibn_majaUrl
                context.getString(R.string.malik) -> url = malikUrl
                context.getString(R.string.muslim) -> url = muslimUrl
                context.getString(R.string.nasai) -> url = nasaiUrl
                context.getString(R.string.trmizi) -> url = trmiziUrl
            }

            return url
        }

        fun getFileName(context: Context, rowaa: String): String{
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

            return fileName
        }

        fun hasEnoughSpaceForFile(context: Context, requiredSize: Long): Boolean {
            val appFilesDir = context.filesDir
            val availableSpace = appFilesDir.usableSpace

            return availableSpace >= requiredSize
        }

        fun getFileStorageSize(context: Context, fileName: String): Long {
            val appFilesDir = context.filesDir
            val file = File(appFilesDir, fileName)
            return file.length()
        }

    }
}