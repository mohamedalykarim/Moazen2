package mohalim.islamic.alarm.alert.moazen.ui.hadith.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.model.Hadith
import mohalim.islamic.alarm.alert.moazen.core.utils.HadithUtils
import mohalim.islamic.alarm.alert.moazen.ui.quran.viewer.QuranViewerActivity

@AndroidEntryPoint
class HadithViewerActivity : AppCompatActivity() {
    val viewModel: HadithViewerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rawy = intent.getStringExtra("RAWY_HADITH")

        runBlocking {
            withContext(Dispatchers.IO){
                val bufferedReader = this@HadithViewerActivity.openFileInput(HadithUtils.getFileName(this@HadithViewerActivity, rawy.toString())+".json").bufferedReader()
                viewModel.setHadiths(Gson().fromJson(bufferedReader, object : TypeToken<MutableList<Hadith>>() {}.type))
                bufferedReader.close()
            }
        }





        setContent {
            HadithViewerActivityUI(viewModel)
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HadithViewerActivityUI(viewModel: HadithViewerViewModel){
    val hadiths by viewModel.hadiths.collectAsState()

    val pagerState = rememberPagerState(pageCount = {
        viewModel.hadiths.value.size
    })

    HorizontalPager(state = pagerState) { page ->

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
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


                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                    ){
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.transparent_bg),
                            contentScale = ContentScale.Crop,
                            contentDescription = ""
                        )

                        Column {
                            Text(text = hadiths[page].hadith,
                                Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                                    .wrapContentHeight(align = Alignment.CenterVertically),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 26.sp
                            )

                            Text(text = hadiths[page].description,
                                Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                                    .wrapContentHeight(align = Alignment.CenterVertically),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )

                        }

                    }





                }

            }

        }



    }




}