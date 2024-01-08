package mohalim.islamic.alarm.alert.moazen.ui.more

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreScreenActivity : AppCompatActivity() {

    private val viewModel : MoreScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { 
            MoreScreenUI(this@MoreScreenActivity, viewModel)
        }
    }
}


@Composable
fun MoreScreenUI(context: Context, viewModel: MoreScreenViewModel) {
    Column {
        LazyVerticalGrid(columns = GridCells.Fixed(count = 3)){

        }
    }
}