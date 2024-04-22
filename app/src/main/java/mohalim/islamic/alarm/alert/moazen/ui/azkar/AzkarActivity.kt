package mohalim.islamic.alarm.alert.moazen.ui.azkar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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
            AzkarActivityUI(viewModel)
        }


        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                if (viewModel.currentZekr.value == null){
                    finish()
                } else {
                    viewModel.setCurrentZekr(null)
                }
            }
        }
        onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )

    }




}



@Composable
fun AzkarActivityUI(viewModel: AzkarViewModel) {
    val currentZekr by viewModel.currentZekr.collectAsState()
    val azkar by viewModel.azkar.collectAsState()
    val showAddZekrSheet by viewModel.showAddZekrSheet.collectAsState()

    var resetSebha by remember { mutableStateOf(false) }


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
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {

           if (currentZekr == null){

               viewModel.getAllAzkarFromRoom()
               LazyColumn(modifier = Modifier.padding(bottom = 10.dp).weight(1f, false)) {
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
                           Row{
                               Image(painterResource(id = R.drawable.azkar_icon2), contentDescription = "azkar icon",
                                   Modifier
                                       .padding(top=5.dp, bottom = 5.dp)
                                       .height(40.dp)
                                       .width(40.dp))
                               Text(text = azkar[index].zekrString,
                                   Modifier
                                       .weight(3f)
                                       .padding(start = 10.dp, end = 10.dp)
                                       .height(50.dp)
                                       .wrapContentHeight(align = Alignment.CenterVertically),  textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                               Text(text = "${azkar[index].count}",
                                   Modifier
                                       .weight(1f)
                                       .padding(start = 5.dp, end = 5.dp)
                                       .height(50.dp)
                                       .wrapContentHeight(align = Alignment.CenterVertically),  textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                           }
                       }

                   }
               }

               Column(modifier = Modifier.height(40.dp)) {
                   SettingButton(name = "Add New", iconId = R.drawable.ic_masjed_icon, onClickCard = {
                       viewModel.setShowAddZekrSheet(true)
                   })
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


                   Sebha(viewModel, currentZekr!!, resetSebha, onResetSebha = {
                       resetSebha = false
                   })

                   SettingButton(name = "Reset", iconId = R.drawable.ic_masjed_icon, onClickCard = {
                       viewModel.resetCounter(currentZekr!!)
                       resetSebha = true
                   })

               }



           }

            if (showAddZekrSheet){
                AddNewZekrBottomSheet(viewModel = viewModel)
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewZekrBottomSheet(viewModel: AzkarViewModel){
    val sheetState = rememberModalBottomSheetState()
    val zekrString = remember { mutableStateOf(TextFieldValue("")) }



    ModalBottomSheet(
        modifier = Modifier.padding(10.dp),
        onDismissRequest = {
            viewModel.setShowAddZekrSheet(false)
        },
        sheetState = sheetState
    ) {

        Text(
            "Add New zekr", modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 16.dp)
                .fillMaxWidth(), textAlign = TextAlign.Center
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 8.dp, end = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(android.graphics.Color.parseColor("#f5ceda")),
                unfocusedBorderColor = Color(android.graphics.Color.parseColor("#f5ceda")),
            ),
            placeholder = {
                Text("Type zekr here", fontSize = 12.sp, color = Color(android.graphics.Color.parseColor("#b5b5b5")))
            },
            textStyle = TextStyle.Default.copy(fontSize = 12.sp),

            value = zekrString.value,
            onValueChange = { value ->
                zekrString.value = value
            }
        )
        Spacer(modifier = Modifier.height(16.dp))


        SettingButton(name = "Add new", iconId = R.drawable.ic_masjed_icon, onClickCard = {
            viewModel.addNewZekr(zekrString.value.text)
            viewModel.getAllAzkarFromRoom()
            viewModel.setShowAddZekrSheet(false)

        } )

        Spacer(modifier = Modifier.height(50.dp))

    }


}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun Sebha(viewModel: AzkarViewModel, currentZekr: AzkarEntity, resetSebha: Boolean, onResetSebha : ()-> Unit){
    val count by viewModel.count.collectAsState()

    val image = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector)
    val imageCircle1 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle1)
    val imageCircle2 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle2)
    val imageCircle3 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle3)
    val imageCircle4 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle4)
    val imageCircle5 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle5)
    val imageCircle6 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle6)
    val imageCircle7 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle7)
    val imageCircle8 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle8)
    val imageCircle9 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle9)
    val imageCircle10 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle10)
    val imageCircle11 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle11)
    val imageCircle12 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle12)
    val imageCircle13 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle13)
    val imageCircle14 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle14)
    val imageCircle15 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle15)
    val imageCircle16 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle16)
    val imageCircle17 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle17)
    val imageCircle18 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle18)
    val imageCircle19 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle19)
    val imageCircle20 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle20)
    val imageCircle21 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle21)
    val imageCircle22 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle22)
    val imageCircle23 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle23)
    val imageCircle24 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle24)
    val imageCircle25 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle25)
    val imageCircle26 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle26)
    val imageCircle27 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle27)
    val imageCircle28 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle28)
    val imageCircle29 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle29)
    val imageCircle30 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle30)
    val imageCircle31 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle31)
    val imageCircle32 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle32)
    val imageCircle33 = AnimatedImageVector.animatedVectorResource(id = R.drawable.azkar_animated_vector_circle33)
    var atEnd1 by remember { mutableStateOf(false) }
    var atEnd2 by remember { mutableStateOf(false) }
    var atEnd3 by remember { mutableStateOf(false) }
    var atEnd4 by remember { mutableStateOf(false) }
    var atEnd5 by remember { mutableStateOf(false) }
    var atEnd6 by remember { mutableStateOf(false) }
    var atEnd7 by remember { mutableStateOf(false) }
    var atEnd8 by remember { mutableStateOf(false) }
    var atEnd9 by remember { mutableStateOf(false) }
    var atEnd10 by remember { mutableStateOf(false) }
    var atEnd11 by remember { mutableStateOf(false) }
    var atEnd12 by remember { mutableStateOf(false) }
    var atEnd13 by remember { mutableStateOf(false) }
    var atEnd14 by remember { mutableStateOf(false) }
    var atEnd15 by remember { mutableStateOf(false) }
    var atEnd16 by remember { mutableStateOf(false) }
    var atEnd17 by remember { mutableStateOf(false) }
    var atEnd18 by remember { mutableStateOf(false) }
    var atEnd19 by remember { mutableStateOf(false) }
    var atEnd20 by remember { mutableStateOf(false) }
    var atEnd21 by remember { mutableStateOf(false) }
    var atEnd22 by remember { mutableStateOf(false) }
    var atEnd23 by remember { mutableStateOf(false) }
    var atEnd24 by remember { mutableStateOf(false) }
    var atEnd25 by remember { mutableStateOf(false) }
    var atEnd26 by remember { mutableStateOf(false) }
    var atEnd27 by remember { mutableStateOf(false) }
    var atEnd28 by remember { mutableStateOf(false) }
    var atEnd29 by remember { mutableStateOf(false) }
    var atEnd30 by remember { mutableStateOf(false) }
    var atEnd31 by remember { mutableStateOf(false) }
    var atEnd32 by remember { mutableStateOf(false) }
    var atEnd33 by remember { mutableStateOf(false) }

    var index by remember { mutableIntStateOf(1) }


    val atEnd by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }

    if (resetSebha){
        atEnd1 = false; atEnd2 = false;atEnd3 = false;atEnd4 =
            false;atEnd5 = false;atEnd6 = false; atEnd7 = false; atEnd8 =
            false; atEnd9 = false;atEnd10 = false;atEnd11 = false;atEnd12 =
            false
        atEnd13 = false; atEnd14 = false; atEnd15 = false; atEnd16 =
            false; atEnd17 = false; atEnd18 = false; atEnd19 =
            false; atEnd20 = false; atEnd21 = false; atEnd22 =
            false; atEnd23 = false; atEnd24 = false
        atEnd25 = false; atEnd26 = false; atEnd27 = false; atEnd28 =
            false; atEnd29 = false; atEnd30 = false; atEnd31 =
            false; atEnd32 = false;atEnd33 = false

        index = 1

        onResetSebha()
    }

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
                        when (index) {
                            1 -> atEnd1 = true
                            2 -> atEnd2 = true
                            3 -> atEnd3 = true
                            4 -> atEnd4 = true
                            5 -> atEnd5 = true
                            6 -> atEnd6 = true
                            7 -> atEnd7 = true
                            8 -> atEnd8 = true
                            9 -> atEnd9 = true
                            10 -> atEnd10 = true
                            11 -> atEnd11 = true
                            12 -> atEnd12 = true
                            13 -> atEnd13 = true
                            14 -> atEnd14 = true
                            15 -> atEnd15 = true
                            16 -> atEnd16 = true
                            17 -> atEnd17 = true
                            18 -> atEnd18 = true
                            19 -> atEnd19 = true
                            20 -> atEnd20 = true
                            21 -> atEnd21 = true
                            22 -> atEnd22 = true
                            23 -> atEnd23 = true
                            24 -> atEnd24 = true
                            25 -> atEnd25 = true
                            26 -> atEnd26 = true
                            27 -> atEnd27 = true
                            28 -> atEnd28 = true
                            29 -> atEnd29 = true
                            30 -> atEnd30 = true
                            31 -> atEnd31 = true
                            32 -> atEnd32 = true
                            33 -> atEnd33 = true
                            34 -> {
                                atEnd1 = true; atEnd2 = false;atEnd3 = false;atEnd4 =
                                    false;atEnd5 = false;atEnd6 = false; atEnd7 = false; atEnd8 =
                                    false; atEnd9 = false;atEnd10 = false;atEnd11 = false;atEnd12 =
                                    false
                                atEnd13 = false; atEnd14 = false; atEnd15 = false; atEnd16 =
                                    false; atEnd17 = false; atEnd18 = false; atEnd19 =
                                    false; atEnd20 = false; atEnd21 = false; atEnd22 =
                                    false; atEnd23 = false; atEnd24 = false
                                atEnd25 = false; atEnd26 = false; atEnd27 = false; atEnd28 =
                                    false; atEnd29 = false; atEnd30 = false; atEnd31 =
                                    false; atEnd32 = false;atEnd33 = false

                                index = 1
                            }
                        }

                        viewModel.updateZekrCounter(currentZekr)

                        index += 1
                    }
                ),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle1, atEnd1),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle2, atEnd2),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle3, atEnd3),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle4, atEnd4),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle5, atEnd5),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle6, atEnd6),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle7, atEnd7),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle8, atEnd8),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle9, atEnd9),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle10, atEnd10),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle11, atEnd11),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle12, atEnd12),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle13, atEnd13),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle14, atEnd14),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle15, atEnd15),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle16, atEnd16),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle17, atEnd17),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle18, atEnd18),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle19, atEnd19),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle20, atEnd20),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle21, atEnd21),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle22, atEnd22),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle23, atEnd23),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle24, atEnd24),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle25, atEnd25),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle26, atEnd26),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle27, atEnd27),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle28, atEnd28),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle29, atEnd29),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle30, atEnd30),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle31, atEnd31),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle32, atEnd32),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = rememberAnimatedVectorPainter(imageCircle33, atEnd33),
            contentDescription = "Timer",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
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

