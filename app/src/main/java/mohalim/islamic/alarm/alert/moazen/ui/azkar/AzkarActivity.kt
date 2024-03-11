package mohalim.islamic.alarm.alert.moazen.ui.azkar

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AzkarActivity : AppCompatActivity() {
    private val viewModel : AzkarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AzkarActivityUI(this@AzkarActivity, viewModel)
        }
    }
}

@Composable
fun AzkarActivityUI(context: Context, viewModel: AzkarViewModel) {

}