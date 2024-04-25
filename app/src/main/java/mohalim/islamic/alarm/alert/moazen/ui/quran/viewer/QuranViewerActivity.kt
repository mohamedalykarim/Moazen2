package mohalim.islamic.alarm.alert.moazen.ui.quran.viewer

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.R
import java.io.BufferedInputStream
import java.io.InputStream


class QuranViewerActivity : AppCompatActivity() {
    private lateinit var splitInstallManager : SplitInstallManager
    private val quranModuleName by lazy { getString(R.string.title_quran) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splitInstallManager = SplitInstallManagerFactory.create(this)

        if(!splitInstallManager.installedModules.contains(quranModuleName)){
            finish()
        }

        setContent { 
            QuranViewerActivityUI(this@QuranViewerActivity, splitInstallManager, quranModuleName)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuranViewerActivityUI(
    context: Context,
    splitInstallManager: SplitInstallManager,
    quranModuleName: String
) {
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = {
        604
    })

    LaunchedEffect(key1 = 1){
        scope.launch {
            pagerState.scrollToPage(603)
        }
    }





    HorizontalPager(state = pagerState) { page ->
        var currentPage = 604 - page

        Log.d("TAG", "QuranViewerActivityUI: "+currentPage)


        Box(modifier = Modifier.fillMaxSize()){
            var highlightedAyaNumber by remember{ mutableIntStateOf(0) }

            
            val resourceId = context.resources.getIdentifier("page$currentPage", "drawable", "mohalim.islamic.alarm.alert.moazen.Quran")

            Image(painterResource(id = resourceId), contentDescription = "Quran page", modifier = Modifier.fillMaxSize())
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