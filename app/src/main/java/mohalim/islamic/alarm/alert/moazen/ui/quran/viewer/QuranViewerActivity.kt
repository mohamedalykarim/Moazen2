package mohalim.islamic.alarm.alert.moazen.ui.quran.viewer

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.model.quran.AyahApi
import mohalim.islamic.alarm.alert.moazen.core.model.quran.PageApi
import java.util.Locale

@AndroidEntryPoint
class QuranViewerActivity : AppCompatActivity() {
    val viewmodel : QuranViewerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val page = intent.getIntExtra("page", 1)
        setContent { 
            QuranViewerActivityUI(this@QuranViewerActivity, viewmodel, page)
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
fun QuranViewerActivityUI(context: Context, viewmodel: QuranViewerViewModel, initialPage: Int) {
    val language = Locale.getDefault().language
    val pageContent by viewmodel.pageContent.collectAsState()
    val isLoading by viewmodel.isLoading.collectAsState()
    val showDownloadPrompt by viewmodel.showDownloadPrompt.collectAsState()
    val isDownloading by viewmodel.isDownloading.collectAsState()
    val downloadProgress by viewmodel.downloadProgress.collectAsState()
    val quranFont by viewmodel.quranFont.collectAsState()

    val pagerState = rememberPagerState(pageCount = { 604 })

    LaunchedEffect(initialPage) {
        val targetPage = if (language == "ar") initialPage - 1 else 604 - initialPage
        pagerState.scrollToPage(targetPage)
    }

    val currentPageNumber = if (language == "ar") pagerState.currentPage + 1 else 604 - pagerState.currentPage
    
    LaunchedEffect(pagerState.currentPage) {
        viewmodel.setLastPage(currentPageNumber)
        viewmodel.fetchPage(currentPageNumber)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().background(Color(0xFFFFFDF7))
        ) { pageIndex ->
            val pageNum = if (language == "ar") pageIndex + 1 else 604 - pageIndex
            val content = pageContent[pageNum]

            Box(modifier = Modifier.fillMaxSize()) {
                if (content == null && isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (content != null) {
                    QuranPageContent(content, quranFont)
                }
            }
        }

        if (isDownloading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp)).padding(24.dp)
                ) {
                    Text("جاري تحميل المصحف للاستخدام بدون إنترنت...", fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(
                        progress = downloadProgress,
                        modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()
                    )
                    Text("${(downloadProgress * 100).toInt()}%")
                }
            }
        }

        if (showDownloadPrompt) {
            AlertDialog(
                onDismissRequest = { viewmodel.dismissDownloadPrompt() },
                title = { Text("تحميل المصحف") },
                text = { Text("هل تريد تحميل صفحات المصحف كاملة (604 صفحة) لتعمل بدون الحاجة للاتصال بالإنترنت لاحقاً؟") },
                confirmButton = {
                    Button(onClick = { viewmodel.startFullDownload() }) { Text("تحميل الآن") }
                },
                dismissButton = {
                    TextButton(onClick = { viewmodel.dismissDownloadPrompt() }) { Text("لاحقاً") }
                }
            )
        }
    }
}

@Composable
fun QuranPageContent(pageData: PageApi, quranFont: String) {
    var selectedAyahNumber by remember { mutableStateOf(-1) }

    val fontFamily = when(quranFont) {
        "hafs_smart" -> FontFamily(Font(R.font.hafs_smart_8))
        "hafs_18" -> FontFamily(Font(R.font.hafs_18))
        "warsh" -> FontFamily(Font(R.font.warsh_10))
        "qaloon" -> FontFamily(Font(R.font.qaloon_10))
        "doori" -> FontFamily(Font(R.font.doori_9))
        "soosi" -> FontFamily(Font(R.font.soosi_9))
        "shouba" -> FontFamily(Font(R.font.shouba_8))
        "bazzi" -> FontFamily(Font(R.font.bazzi_7))
        "qumbul" -> FontFamily(Font(R.font.qumbul_7))
        else -> FontFamily(Font(R.font.hafs_smart_8))

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "الجزء ${pageData.ayahs.first().juz}", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037))
            Text(text = "صفحة ${pageData.number}", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037))
        }

        val annotatedString = buildAnnotatedString {
            pageData.ayahs.forEach { ayah ->
                val isSelected = selectedAyahNumber == ayah.number
                
                pushStringAnnotation(tag = "AYAH", annotation = ayah.number.toString())
                withStyle(style = SpanStyle(
                    background = if (isSelected) Color(0xFFFFE082) else Color.Transparent,
                    color = Color.Black,
                    fontSize = 24.sp
                )) {
                    append(ayah.text)
                }
                
                withStyle(style = SpanStyle(color = Color(0xFF8D6E63), fontSize = 18.sp)) {
                    append(" \uFD3F${ayah.numberInSurah}\uFD3E ")
                }
                pop()
            }
        }

        Text(
            text = annotatedString,
            lineHeight = 45.sp,
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    // This is a simplified tap detection for Span
                }
        )
    }
}
