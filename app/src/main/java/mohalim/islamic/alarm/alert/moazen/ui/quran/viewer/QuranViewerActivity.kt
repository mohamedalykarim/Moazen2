package mohalim.islamic.alarm.alert.moazen.ui.quran.viewer

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.R

class QuranViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { 
            QuranViewerActivityUI(this@QuranViewerActivity)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuranViewerActivityUI(context: Context) {
    val pagerState = rememberPagerState(pageCount = {
        604
    })

    runBlocking {
        withContext(Dispatchers.IO){
            pagerState.scrollToPage(604)
        }
    }

    HorizontalPager(state = pagerState) { page ->
        var currentPage = 605 - page

        Box(modifier = Modifier.fillMaxSize()){
            var highlightedAyaNumber by remember{ mutableIntStateOf(0) }

            val drawableResId = context.resources.getIdentifier(
                "quran$currentPage.png",
                "drawable",
                "mohalim.islamic.alarm.alert.moazen.QuranResources"
            )

            Image(painterResource(id = drawableResId), contentDescription = "Quran page", modifier = Modifier.fillMaxSize())
            val highlightArea = IntOffset(280,480)
            val highlightSize=  IntOffset(215,50)

            val ayaNumber = 1

            HighlightArea(highlightArea = highlightArea, highlightSize, highlightedAyaNumber, ayaNumber, onClickCanvas = {
                if (highlightedAyaNumber == ayaNumber){
                    highlightedAyaNumber = 0
                }else{
                    highlightedAyaNumber = ayaNumber
                }
            })
        }

    }





}


@Composable
fun HighlightArea(
    highlightArea: IntOffset,
    highlightSize: IntOffset,
    highlightedAyaNumber : Int,
    ayaNumber : Int,
    onClickCanvas : () -> Unit
){
    val interactionSetting = remember { MutableInteractionSource() }
    val highlightedColor = if (highlightedAyaNumber == ayaNumber) android.graphics.Color.parseColor("#4c000000") else android.graphics.Color.parseColor("#0000ffff")
    Canvas(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = interactionSetting,
            indication = null,
            onClick = onClickCanvas
        )
    ) {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            canvas.nativeCanvas.apply {
                drawRect(
                    highlightArea.x.toFloat(),
                    highlightArea.y.toFloat(),
                    (highlightArea.x + highlightSize.x).toFloat(),
                    (highlightArea.y + highlightSize.y).toFloat(),
                    paint.apply { color = highlightedColor }
                )
            }
        }
    }
}