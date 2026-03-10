package mohalim.islamic.alarm.alert.moazen.ui.quran.main

import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.ui.quran.viewer.QuranViewerActivity
import javax.inject.Inject

@AndroidEntryPoint
class QuranMainActivity : AppCompatActivity() {
    val viewModel : QuranMainViewModel by viewModels()
    @Inject lateinit var dataStore : DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuranMainActivityUI(this, viewModel)
        }
        viewModel.fetchSurahList()
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            withContext(Dispatchers.IO){
                val pageNumberReference = PreferencesUtils.getPageReference(dataStore)
                viewModel.setPageNumberReference(pageNumberReference)
            }
        }
    }
}

@Composable
fun QuranMainActivityUI(context: Context, viewModel: QuranMainViewModel) {
    val allSurah by viewModel.allSurah.collectAsState()
    val pageNumberReference by viewModel.pageNumberReference.collectAsState()

    val surahStartPages = remember {
        intArrayOf(
            1, 2, 50, 77, 106, 128, 151, 177, 187, 208, 221, 235, 249, 255, 262, 267, 282, 293, 305, 312, 322, 332, 342, 350, 359, 367, 377, 385, 396, 404,
            411, 415, 418, 428, 434, 440, 446, 453, 458, 467, 477, 483, 489, 496, 499, 502, 507, 511, 515, 518, 520, 523, 526, 528, 531, 534, 537, 542, 545, 549,
            551, 553, 554, 556, 558, 560, 562, 564, 566, 568, 570, 572, 574, 575, 577, 578, 580, 582, 583, 585, 587, 589, 591, 593, 594, 595, 597, 598, 599, 600,
            601, 601, 602, 602, 603, 603, 604, 604, 605, 605, 606, 606, 607, 607, 608, 608, 609, 609, 610, 610, 611, 611, 612, 612
        ) 
    }

    Box(
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

        if (allSurah.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn {
                item {
                    ReferenceCard(context, pageNumberReference)
                }

                itemsIndexed(allSurah) { index, surah ->
                    SurahItem(context, surah, surahStartPages.getOrElse(index) { 1 })
                }
            }
        }
    }
}

@Composable
fun ReferenceCard(context: Context, pageNumberReference: Int) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSetting = remember { MutableInteractionSource() }
    LaunchedEffect(interactionSetting) {
        interactionSetting.interactions.collect { interaction ->
            if (interaction is PressInteraction.Press) {
                isPressed = true
                Handler(Looper.getMainLooper()).postDelayed({ isPressed = false }, 100)
            }
        }
    }

    val settingScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 80, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f)),
        label = "settingScale"
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .scale(settingScale)
            .background(Color(parseColor("#521f58")), RoundedCornerShape(12.dp))
            .border(1.dp, Color(parseColor("#ffe7ee")), shape = RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSetting,
                indication = null,
                onClick = {
                    val intent = Intent(context, QuranViewerActivity::class.java)
                    intent.putExtra("page", pageNumberReference)
                    context.startActivity(intent)
                }
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.transparent_bg),
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$pageNumberReference",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(parseColor("#ffe7ee")),
                    fontSize = 40.sp
                )
                Text(
                    text = stringResource(R.string.current_referenced_page),
                    color = Color(parseColor("#dadada")),
                    fontSize = 14.sp
                )
            }

            Image(
                modifier = Modifier.padding(10.dp).size(60.dp).align(Alignment.CenterStart),
                painter = painterResource(id = R.drawable.reference_icon),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun SurahItem(context: Context, surah: mohalim.islamic.alarm.alert.moazen.core.model.quran.SurahApi, startPage: Int) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSetting = remember { MutableInteractionSource() }
    LaunchedEffect(interactionSetting) {
        interactionSetting.interactions.collect { interaction ->
            if (interaction is PressInteraction.Press) {
                isPressed = true
                Handler(Looper.getMainLooper()).postDelayed({ isPressed = false }, 120)
            }
        }
    }

    val settingScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 80, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f)),
        label = "settingScale"
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .scale(settingScale)
            .background(Color(parseColor("#fff2f6")), RoundedCornerShape(8.dp))
            .border(1.dp, Color(parseColor("#ffd4e2")), shape = RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = interactionSetting,
                indication = null,
                onClick = {
                    val intent = Intent(context, QuranViewerActivity::class.java)
                    intent.putExtra("page", startPage)
                    context.startActivity(intent)
                }
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(80.dp).padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painterResource(id = R.drawable.zokhrof),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(text = "${surah.number}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = surah.englishName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "${surah.revelationType} - ${surah.numberOfAyahs} Ayahs", fontSize = 12.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = surah.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(parseColor("#521f58")),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
