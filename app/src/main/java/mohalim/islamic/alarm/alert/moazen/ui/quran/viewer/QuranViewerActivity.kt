package mohalim.islamic.alarm.alert.moazen.ui.quran.viewer

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.R
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class QuranViewerActivity : AppCompatActivity() {
    private lateinit var splitInstallManager : SplitInstallManager
    private val quranModuleName by lazy { getString(R.string.title_quran) }
    @Inject lateinit var dataStore: DataStore<Preferences>

    val viewmodel : QuranViewerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splitInstallManager = SplitInstallManagerFactory.create(this)

        if(!splitInstallManager.installedModules.contains(quranModuleName)){
            finish()
        }

        val page = intent.getIntExtra("Surah", 1)

        setContent { 
            QuranViewerActivityUI(this@QuranViewerActivity, viewmodel, splitInstallManager, quranModuleName, page, dataStore)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                viewmodel.setPreferencesPageReference()
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuranViewerActivityUI(
    context: Context,
    viewmodel: QuranViewerViewModel,
    splitInstallManager: SplitInstallManager,
    quranModuleName: String,
    surahPage: Int,
    dataStore: DataStore<Preferences>
) {
    val scope = rememberCoroutineScope()
    val language = Locale.getDefault().language;


    val pagerState = rememberPagerState(pageCount = {
        604
    })

    LaunchedEffect(key1 = 1){
        scope.launch {
            if (language == "en") pagerState.scrollToPage(604- surahPage)
            if (language == "ar") pagerState.scrollToPage(surahPage - 1)
        }
    }

    if (language == "en") viewmodel.setLastPage(604 - pagerState.currentPage)
    if (language == "ar") viewmodel.setLastPage(pagerState.currentPage + 1)

    var scale by remember { mutableFloatStateOf(1f) }
    var isZoomable by remember { mutableStateOf(false) }

    HorizontalPager(
        modifier = Modifier.pointerInput(Unit) {
            detectTransformGestures{ centroid, pan, zoom, rotation ->
                scale *= zoom
            }

        },
        state = pagerState) { page ->
        Log.d("TAG", "QuranViewerActivityUI: page $page ")
        var currentPage = if(language == "en"){
            604 - page
        }else if(language == "ar"){
            page + 1
        }else{
            604 - page
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
            ){

                val resourceId = context.resources.getIdentifier("page$currentPage", "drawable", "mohalim.islamic.alarm.alert.moazen.Quran")
                Log.d("TAG", "QuranViewerActivityUI: page$currentPage")
                Image(
                    painterResource(id = resourceId),
                    contentDescription = "Quran page",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            // adding some zoom limits (min 50%, max 200%)
                            scaleX = maxOf(1f, minOf(2f, scale)),
                            scaleY = maxOf(1f, minOf(2f, scale))
                        )
                )

//            val scale by viewmodel.zoomScale.collectAsState()
//            val offset by viewmodel.zoomOffset.collectAsState()


//            var highlightedAyaNumber by remember{ mutableIntStateOf(0) }
//            val highlightArea = IntOffset(Utils.dipTopx(context, 150f),Utils.dipTopx(context, 347f))
//            val highlightSize=  IntOffset(Utils.dipTopx(context, 115f),Utils.dipTopx(context, 30f))
//            val ayaNumber = 1


//            HighlightArea(viewmodel, highlightArea, highlightSize, highlightedAyaNumber, ayaNumber, scale, offset, onClickCanvas = {
//                if (highlightedAyaNumber == ayaNumber){
//                    highlightedAyaNumber = 0
//                }else{
//                    highlightedAyaNumber = ayaNumber
//                }
//            })
            }


        }



    }





}




@Composable
fun HighlightArea(
    viewmodel: QuranViewerViewModel,
    highlightArea: IntOffset,
    highlightSize: IntOffset,
    highlightedAyaNumber : Int,
    ayaNumber : Int,
    scale : Float,
    offset : Offset,
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