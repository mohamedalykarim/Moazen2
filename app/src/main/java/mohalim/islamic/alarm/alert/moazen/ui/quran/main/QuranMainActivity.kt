package mohalim.islamic.alarm.alert.moazen.ui.quran.main

import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import mohalim.islamic.alarm.alert.moazen.core.utils.Utils
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.startToGetAllSurahMetaData(this)

        runBlocking {
            withContext(Dispatchers.IO){
                val pageNumberReference = PreferencesUtils.getPageReference(dataStore)
                viewModel.setPageNumberReference(pageNumberReference)

                val pageReference = Utils.getPageData(this@QuranMainActivity, pageNumberReference)
                viewModel.setPageReference(pageReference)
            }
        }
    }
}

@Composable
fun QuranMainActivityUI(context: Context, viewModel: QuranMainViewModel) {
    val allSurah by viewModel.allSurah.collectAsState()
    val pageNumberReference by viewModel.pageNumberReference.collectAsState()
    val pageReference by viewModel.pageReference.collectAsState()

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

        LazyColumn{

            item {
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .background(Color(parseColor("#521f58")))
                        .border(
                            1.dp,
                            Color(parseColor("#ffe7ee")),
                            shape = RoundedCornerShape(5)
                        )
                        .clickable {
                            val intent = Intent(context, QuranViewerActivity::class.java)
                            intent.putExtra("Surah", pageNumberReference)
                            context.startActivity(intent)
                        }

                ) {

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                    ){
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(id = R.drawable.transparent_bg),
                            contentScale = ContentScale.Crop,
                            contentDescription = ""
                        )

                        Text(text = "$pageNumberReference",
                            Modifier
                                .padding(top = 0.dp, start = 60.dp)
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                                .height(70.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(parseColor("#ffe7ee")),
                            fontSize = 50.sp
                        )

                        Text(text = "سورة "+pageReference.endSurahArName,
                            Modifier
                                .padding(top = 50.dp, start = 60.dp)
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                                .height(50.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(parseColor("#ffffff")),
                            fontSize = 25.sp
                        )

                        Text(text = stringResource(R.string.current_referenced_page),
                            Modifier
                                .padding(top = 80.dp, start = 60.dp)
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                                .height(50.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            textAlign = TextAlign.Center,
                            color = Color(parseColor("#dadada")),
                            fontSize = 11.sp
                        )

                        Image(
                            modifier = Modifier
                                .padding(top = 0.dp, start = 10.dp)
                                .width(90.dp)
                                .height(120.dp),
                            painter = painterResource(id = R.drawable.reference_icon),
                            contentDescription = ""
                        )



                    }

                }
            }


            items(allSurah.size){index->
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
                        .clickable {
                            val intent = Intent(context, QuranViewerActivity::class.java)
                            intent.putExtra("Surah", allSurah[index].page)
                            context.startActivity(intent)
                        }

                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                    ){
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.transparent_bg),
                            contentScale = ContentScale.Crop,
                            contentDescription = ""
                        )

                        Row{
                            Image(
                                painterResource(id = R.drawable.zokhrof),
                                contentDescription = "Image",
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(120.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f, true)
                                    .height(70.dp)
                            ) {
                                Box {
                                    val surahName = allSurah[index].titleAr
                                    Text(text = "سورة $surahName",
                                        Modifier
                                            .padding(top = 10.dp)
                                            .fillMaxWidth()
                                            .padding(start = 10.dp, end = 10.dp)
                                            .height(50.dp)
                                            .wrapContentHeight(align = Alignment.CenterVertically),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 26.sp
                                    )

                                    var surahType = ""
                                    if(allSurah[index].type == "Makkiyah") surahType = "مكية"
                                    else if(allSurah[index].type == "Madaniyah") surahType =  "مدنية"

                                    val verseCount = allSurah[index].verseCount

                                    Text(text = "سورة $surahType، عدد آياتها $verseCount",
                                        Modifier
                                            .padding(top = 50.dp)
                                            .fillMaxWidth()
                                            .padding(start = 10.dp, end = 10.dp)
                                            .height(50.dp)
                                            .wrapContentHeight(align = Alignment.CenterVertically),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(120.dp)
                            ) {
                                Text(text = allSurah[index].index,
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp, end = 10.dp)
                                        .height(120.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                )
                            }


                        }

                    }





                }
            }
        }

    }
}