package mohalim.islamic.alarm.alert.moazen.ui.azkar

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.room.entity.AzkarEntity
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingButton

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
    val currentZekr by viewModel.currentZekr.collectAsState()
    val azkar by viewModel.azkar.collectAsState()

    Box(
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
        Column(modifier = Modifier.fillMaxSize()) {

           if (currentZekr == null){
               viewModel.getAllAzkarFromRoom()
               LazyColumn {
                   items(azkar.size){index ->

                       Column(
                           modifier = Modifier
                               .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                               .fillMaxWidth()
                               .background(Color(android.graphics.Color.parseColor("#fff2f6")))
                               .border(
                                   1.dp,
                                   Color(android.graphics.Color.parseColor("#ffd4e2")),
                                   shape = RoundedCornerShape(5)
                               )
                               .padding(10.dp)
                               .clickable {
                                   viewModel.setCurrentZekr(azkar[index])
                               }

                       ) {
                           Row(){
                               Text(text = azkar[index].zekrString,  Modifier.weight(3f),  textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                               Text(text = "${azkar[index].count}",  Modifier.weight(1f),  textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                           }
                       }

                   }
               }
               
           }else{
               Column (
                   modifier = Modifier
                       .fillMaxWidth()
                       .fillMaxHeight(),
                   verticalArrangement = Arrangement.SpaceBetween
               ){
                   Column(
                       modifier = Modifier
                           .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                           .fillMaxWidth()
                           .background(Color(android.graphics.Color.parseColor("#fff2f6")))
                           .border(
                               1.dp,
                               Color(android.graphics.Color.parseColor("#ffd4e2")),
                               shape = RoundedCornerShape(20)
                           )
                           .padding(10.dp),
                   ){
                       Text(
                           modifier = Modifier
                               .padding(start = 16.dp, end = 16.dp)
                               .fillMaxWidth()
                               .wrapContentHeight(align = Alignment.CenterVertically),
                           text = currentZekr!!.zekrString,
                           fontSize = 25.sp,
                           fontWeight = FontWeight.ExtraBold,
                           textAlign = TextAlign.Center,
                           color = Color(android.graphics.Color.parseColor("#66236e"))
                       )

                   }


                   Sebha(viewModel, currentZekr!!)

                   SettingButton(name = "Reset", iconId = R.drawable.ic_masjed_icon, onClickCard = {
                       viewModel.resetCounter(currentZekr!!)
                   })

                   Spacer(modifier = Modifier.height(10.dp))

               }



           }

        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun Sebha(viewModel: AzkarViewModel, currentZekr: AzkarEntity){
    val count by viewModel.count.collectAsState()

    val image = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector)
    var atEnd by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }

    Box (modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
        .height(300.dp)
    ) {
        Image(
            painter = rememberAnimatedVectorPainter(image, atEnd),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClick = {
                        atEnd = !atEnd
                        viewModel.updateZekrCounter(currentZekr)
                    }
                ),
            contentScale = ContentScale.Fit
        )

        /** Counter **/
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .height(400.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
            text = count.toString() ,
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            color = Color(android.graphics.Color.parseColor("#66236e"))
        )

    }




}

