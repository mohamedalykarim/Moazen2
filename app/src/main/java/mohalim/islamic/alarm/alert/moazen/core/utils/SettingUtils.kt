package mohalim.islamic.alarm.alert.moazen.core.utils

import android.content.Context
import androidx.compose.ui.res.stringResource
import mohalim.islamic.alarm.alert.moazen.R
import org.intellij.lang.annotations.Language

class SettingUtils {
    companion object{
        fun getAzanPerformerNameByRawId(context: Context, rawId: Int): String {
            var name = ""
            when(rawId){
                R.raw.hamdoon_hamady->{ name = context.getString(R.string.hamdoon_hamady) }
                R.raw.abdulrahman_mossad->{ name = context.getString(R.string.abdulrahman_mossad)}
                R.raw.elharam_elmekky->{ name = context.getString(R.string.elharam_elmekky) }
                R.raw.hafiz_ahmed->{ name = context.getString(R.string.hafiz_ahmed) }
                R.raw.idris_aslami->{ name = context.getString(R.string.idris_aslami) }
                R.raw.islam_sobhy->{ name = context.getString(R.string.islam_sobhy) }
                R.raw.naser_elkatamy->{ name =context.getString(R.string.naser_elkatamy) }

            }

            return name
        }

        fun getPreAzanPerformerNameByRawId(context: Context, rawId: Int): String {
            var name = ""
            when(rawId){
                R.raw.pre_salah_1->{ name = context.getString(R.string.before_pray_notification_1) }
                R.raw.pre_salah_2->{ name = context.getString(R.string.before_pray_notification_2) }
                R.raw.pre_salah_3->{ name = context.getString(R.string.before_pray_notification_3) }

            }

            return name
        }
    }
}