package mohalim.islamic.alarm.alert.moazen.ui.more

import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingActivity

@AndroidEntryPoint
class MoreScreenActivity : AppCompatActivity() {

    private val viewModel: MoreScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoreScreenUI(this@MoreScreenActivity, viewModel)
        }
    }
}

@Composable
fun MoreScreenUI(context: Context, viewModel: MoreScreenViewModel) {

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(android.graphics.Color.parseColor("#ffffff")))
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.transparent_bg),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )

        Column {
            LazyVerticalGrid(columns = GridCells.Fixed(count = 3)) {

                /** Setting Button **/
                item {
                    val interactionSetting = remember { MutableInteractionSource() }
                    val isSettingPressed by interactionSetting.collectIsPressedAsState()
                    val settingScale by animateFloatAsState(if (isSettingPressed) 0.8f else 1f, label = "settingScale")

                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .scale(settingScale)
                            .clickable(
                                interactionSource = interactionSetting,
                                indication = null,
                                onClick = {
                                    context.startActivity(Intent(context, SettingActivity::class.java))
                                })
                    ) {

                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(
                                    color = if (isSettingPressed) Color(parseColor("#602467")) else Color(
                                        parseColor("#521f58")
                                    )
                                )
                        ) {
                            Image(
                                modifier = Modifier.fillMaxWidth(),
                                painter = painterResource(id = R.drawable.transparent_bg),
                                contentScale = ContentScale.Crop,
                                contentDescription = ""
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Filled.Settings,
                                    tint = Color.White,
                                    contentDescription = "Setting",
                                    modifier = Modifier
                                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                        .size(70.dp)
                                )
                                Text(
                                    text = "Setting",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                )


                            }
                        }
                    }
                }


            }
        }

    }
}