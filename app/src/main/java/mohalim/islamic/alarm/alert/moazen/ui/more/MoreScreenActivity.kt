package mohalim.islamic.alarm.alert.moazen.ui.more

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R

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
        LazyVerticalGrid(columns = GridCells.Fixed(count = 4)){

            /** Setting Button **/
            item {
                val interactionSetting = remember { MutableInteractionSource() }
                val isSettingPressed by interactionSetting.collectIsPressedAsState()
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSetting,
                            indication = null,
                            onClick = {

                            }
                        ),
                ) {
                    Column(modifier= Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Settings, contentDescription = "Setting" ,modifier =  Modifier.padding(top=5.dp, start =5.dp, end=5.dp).size(70.dp))
                        Text(
                            text = "Setting",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            textAlign = TextAlign.Center,
                        )
                    }

                }
            }


        }
    }
}