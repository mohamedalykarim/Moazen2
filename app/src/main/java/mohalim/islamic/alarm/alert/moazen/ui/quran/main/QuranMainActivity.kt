package mohalim.islamic.alarm.alert.moazen.ui.quran.main

import android.content.Context
import android.content.Intent
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
import mohalim.islamic.alarm.alert.moazen.ui.quran.viewer.QuranViewerActivity

@AndroidEntryPoint
class QuranMainActivity : AppCompatActivity() {
    val viewModel : QuranMainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuranMainActivityUI(this, viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startToGetAllSurahMetaData(this)
    }
}

@Composable
fun QuranMainActivityUI(context: Context, viewModel: QuranMainViewModel) {
    val allSurah by viewModel.allSurah.collectAsState()

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

        LazyColumn{
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
                        .padding(10.dp)
                        .clickable {
                            val intent = Intent(context, QuranViewerActivity::class.java)
                            intent.putExtra("Surah", allSurah[index].page)
                            context.startActivity(intent)
                        }

                ) {
                    Text(text = allSurah[index].titleAr,
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                            .height(70.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

    }
}