package mohalim.islamic.alarm.alert.moazen.ui.more

import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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
            .background(color = Color(parseColor("#ffffff")))
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.transparent_bg),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )

        Column {
            LazyVerticalGrid(columns = GridCells.Fixed(count = 3)) {
                /** Azkar **/
                item {
                    ItemContainer("Azkar", R.drawable.azkar_icon, onClickCard = {

                    })
                }

                /** Setting **/
                item {
                    ItemContainer("Setting", R.drawable.setting_icon, onClickCard = {
                        Handler(Looper.getMainLooper()).postDelayed({
                            context.startActivity(Intent(context, SettingActivity::class.java))
                        },200)
                    })
                }
            }
        }

    }
}

@Composable
fun ItemContainer(name: String, iconId : Int, onClickCard : ()-> Unit) {
    var isPressed by remember {
        mutableStateOf(false)
    }
    /** Setting Button **/
    val interactionSetting = remember { MutableInteractionSource() }
    LaunchedEffect(interactionSetting){
        interactionSetting.interactions.collect{interaction->
            when(interaction){
                is PressInteraction.Press ->{
                    isPressed = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        isPressed = false

                    },90)
                }
            }

        }
    }
    val settingScale by animateFloatAsState(
        targetValue =  if (isPressed) 0.5f else 1f,
        animationSpec = tween(durationMillis = 80, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f)),
        label = "settingScale")

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(130.dp)
            .scale(settingScale)
            .clickable(
                interactionSource = interactionSetting,
                indication = null,
                onClick = onClickCard
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isPressed) Color(parseColor("#521f58")) else Color(
                    parseColor("#66236e")
                )
            )
            .border(
                2.dp,
                if (isPressed) Color(parseColor("#ffffff")) else Color(
                    parseColor("#4e1e54")
                ),
                shape = RoundedCornerShape(20.dp)
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
            Image(
                painter = painterResource(id = iconId),
                contentDescription = name,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .size(70.dp)
            )
            Text(
                text = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                color = Color(parseColor("#ffffff")),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )


        }
    }
}
