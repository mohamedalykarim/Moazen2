package mohalim.islamic.alarm.alert.moazen.ui.quran.viewer

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
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
            QuranViewerActivityUI(viewmodel, page)
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
fun QuranViewerActivityUI(viewmodel: QuranViewerViewModel, initialPage: Int) {
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
                    Text("جاري تحميل المصحف والخطوط...", fontWeight = FontWeight.Bold)
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
                text = { Text("هل تريد تحميل صفحات المصحف كاملة (604 صفحة) والخطوط لتعمل بدون الحاجة للاتصال بالإنترنت لاحقاً؟") },
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
fun SurahHeader(name: String, fontFamily: FontFamily) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color(0xFFFDF5E6), RoundedCornerShape(8.dp))
            .border(2.dp, Color(0xFF8D6E63), RoundedCornerShape(8.dp))
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "سُورَةُ $name",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            color = Color(0xFF5D4037),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun QuranPageContent(pageData: PageApi, quranFont: String) {
    var selectedAyahNumber by remember { mutableStateOf(-1) }

    val fontFamily = when(quranFont) {
        "amiri" -> FontFamily(Font(R.font.amiri))
        "kitab" -> FontFamily(Font(R.font.kitab))
        "kitab_bold" -> FontFamily(Font(R.font.kitab_bold))
        else -> FontFamily(Font(R.font.kitab))
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header info
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "الجزء ${pageData.ayahs.firstOrNull()?.juz ?: ""}", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037), fontSize = 14.sp)
                Text(text = "صفحة ${pageData.number}", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037), fontSize = 14.sp)
            }

            // Main Content
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (pageData.number <= 2) Arrangement.Center else Arrangement.SpaceBetween
            ) {
                val ayahs = pageData.ayahs
                var currentSurahId = -1
                val blocks = mutableListOf<List<AyahApi>>()
                var currentBlock = mutableListOf<AyahApi>()
                
                ayahs.forEach { ayah ->
                    if (ayah.surah.number != currentSurahId || ayah.numberInSurah == 1) {
                        if (currentBlock.isNotEmpty()) blocks.add(currentBlock)
                        currentBlock = mutableListOf()
                        currentSurahId = ayah.surah.number
                    }
                    currentBlock.add(ayah)
                }
                if (currentBlock.isNotEmpty()) blocks.add(currentBlock)

                blocks.forEach { block ->
                    val firstAyah = block.first()
                    if (firstAyah.numberInSurah == 1) {
                        SurahHeader(name = firstAyah.surah.name, fontFamily = fontFamily)
                    }

                    AyahFlowText(
                        ayahs = block,
                        fontFamily = fontFamily,
                        selectedAyahNumber = selectedAyahNumber,
                        onAyahSelected = { selectedAyahNumber = it },
                        pageNumber = pageData.number
                    )
                }
            }
        }
    }
}

@Composable
fun AyahFlowText(
    ayahs: List<AyahApi>,
    fontFamily: FontFamily,
    selectedAyahNumber: Int,
    onAyahSelected: (Int) -> Unit,
    pageNumber: Int
) {
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val basmalaText = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ "
    val basmalaAlt = "بِسۡمِ ٱللَّهِ ٱلرَّحۡمَـٰنِ ٱلرَّحِیمِ"

    val annotatedString = buildAnnotatedString {
        ayahs.forEach { ayah ->
            val isSelected = selectedAyahNumber == ayah.number
            var ayahText = ayah.text
            
            if (ayah.numberInSurah == 1 && ayah.surah.number != 1 && ayah.surah.number != 9) {
                val cleanBasmala = if (ayahText.contains(basmalaText.trim())) basmalaText.trim() else if (ayahText.contains(basmalaAlt)) basmalaAlt else null
                cleanBasmala?.let {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)) {
                        append("\n$it\n")
                    }
                    ayahText = ayahText.replace(it, "").trim()
                }
            }

            pushStringAnnotation(tag = "AYAH", annotation = ayah.number.toString())
            withStyle(style = SpanStyle(
                background = if (isSelected) Color(0xFFFFE082) else Color.Transparent,
                color = Color.Black,
                fontSize = 21.sp
            )) {
                append(ayahText)
            }

            withStyle(style = SpanStyle(color = Color(0xFF8D6E63), fontSize = 15.sp)) {
                append(" \uFD3F${ayah.numberInSurah}\uFD3E ")
            }
            pop()
        }
    }

    Text(
        text = annotatedString,
        lineHeight = if (pageNumber <= 2) 55.sp else 46.sp,
        textAlign = if (pageNumber <= 2) TextAlign.Center else TextAlign.Justify,
        fontFamily = fontFamily,
        onTextLayout = { textLayoutResult = it },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    textLayoutResult?.let { layoutResult ->
                        val position = layoutResult.getOffsetForPosition(offset)
                        annotatedString.getStringAnnotations(tag = "AYAH", start = position, end = position)
                            .firstOrNull()?.let { annotation ->
                                onAyahSelected(annotation.item.toInt())
                            } ?: onAyahSelected(-1)
                    }
                }
            }
    )
}
