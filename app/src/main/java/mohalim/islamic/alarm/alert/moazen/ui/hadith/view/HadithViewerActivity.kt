package mohalim.islamic.alarm.alert.moazen.ui.hadith.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.utils.HadithUtils

@AndroidEntryPoint
class HadithViewerActivity : AppCompatActivity() {
    val viewModel: HadithViewerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rawy = intent.getStringExtra("RAWY_HADITH")
        val rawyfileName = HadithUtils.getFileName(this, rawy!!)

        viewModel.getCurrentHadithFromRoom(rawyfileName, 1)


        setContent {
            HadithViewerActivityUI(applicationContext, viewModel, rawyfileName)
        }
    }

    override fun onResume() {
        super.onResume()


    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HadithViewerActivityUI(context: Context, viewModel: HadithViewerViewModel, rawyfileName: String){
    val currentHadithNumber by viewModel.currentHadithNumber.collectAsState()
    val currentHadithHadith by viewModel.currentHadithHadith.collectAsState()
    val currentHadithDescription by viewModel.currentHadithDescription.collectAsState()

    val pagerState = rememberPagerState(pageCount = {
        HadithUtils.getHadithCountForRawy(rawyfileName)
    })

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.getCurrentHadithFromRoom(rawyfileName, page+1)
        }
    }

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
                            Box (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(Color(android.graphics.Color.parseColor("#66236e")))
                                    .border(
                                        1.dp, Color(android.graphics.Color.parseColor("#4e1e54")),
                                        shape = RoundedCornerShape(7.dp)
                                    )

                            ) {
                                Image(
                                    modifier = Modifier.fillMaxWidth(),
                                    painter = painterResource(id = R.drawable.transparent_bg),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = ""
                                )

                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_hadith_icon),
                                        contentDescription = "ic_hadith_icon",
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier
                                            .padding(top = 16.dp, start = 8.dp, end = 16.dp)
                                            .size(32.dp)
                                    )
                                    Text(
                                        text = HadithUtils.getRawyName(context, rawyfileName+".json")+" - " + currentHadithNumber,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .padding(4.dp)
                                            .wrapContentHeight(Alignment.CenterVertically),
                                        color = Color(android.graphics.Color.parseColor("#ffffff")),
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                    )


                                }
                            }

                            Text(text = currentHadithHadith,
                                Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                                    .wrapContentHeight(align = Alignment.CenterVertically),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 26.sp
                            )

                            Text(text = currentHadithDescription,
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