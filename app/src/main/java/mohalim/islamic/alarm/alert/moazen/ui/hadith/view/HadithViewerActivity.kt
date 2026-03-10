package mohalim.islamic.alarm.alert.moazen.ui.hadith.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.utils.HadithUtils

@AndroidEntryPoint
class HadithViewerActivity : AppCompatActivity() {
    private val viewModel: HadithViewerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rawy = intent.getStringExtra("RAWY_HADITH") ?: ""
        val rawyfileName = HadithUtils.getFileName(this, rawy)

        viewModel.getCurrentHadithFromRoom(rawyfileName, 1)

        setContent {
            MaterialTheme {
                HadithViewerActivityUI(viewModel, rawyfileName) { finish() }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HadithViewerActivityUI(
    viewModel: HadithViewerViewModel,
    rawyfileName: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val currentHadithNumber by viewModel.currentHadithNumber.collectAsState()
    val currentHadithHadith by viewModel.currentHadithHadith.collectAsState()
    val currentHadithDescription by viewModel.currentHadithDescription.collectAsState()

    val totalHadithCount = remember(rawyfileName) {
        HadithUtils.getHadithCountForRawy(rawyfileName)
    }

    val pagerState = rememberPagerState(pageCount = { totalHadithCount })
    val scope = rememberCoroutineScope()
    var showJumpDialog by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.getCurrentHadithFromRoom(rawyfileName, pagerState.currentPage + 1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        HadithUtils.getRawyName(context, "$rawyfileName.json"),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showJumpDialog = true }) {
                        Icon(Icons.Default.Numbers, contentDescription = "Go to Hadith")
                    }
                    IconButton(onClick = {
                        shareHadith(context, currentHadithHadith, currentHadithDescription)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background Image/Texture
            Image(
                painter = painterResource(id = R.drawable.transparent_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.05f
            )

            Column(modifier = Modifier.fillMaxSize()) {
                // Progress Indicator
                LinearProgressIndicator(
                    progress = { (pagerState.currentPage + 1).toFloat() / totalHadithCount },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    pageSpacing = 16.dp
                ) { page ->
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Hadith Badge
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.padding(bottom = 24.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.hadith) + " " + currentHadithNumber,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            // Ornament icon
                            Icon(
                                painter = painterResource(id = R.drawable.ic_hadith_icon),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Hadith Text
                            Text(
                                text = currentHadithHadith,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    lineHeight = 38.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // Divider with ornament
                            Row(
                                modifier = Modifier.fillMaxWidth(0.6f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                                Box(modifier = Modifier.padding(horizontal = 8.dp).size(6.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)))
                                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Description/Sanad
                            Text(
                                text = currentHadithDescription,
                                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Bottom Page Counter
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp, top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                        shape = CircleShape,
                        onClick = { showJumpDialog = true }
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1} / $totalHadithCount",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }

    if (showJumpDialog) {
        var textValue by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showJumpDialog = false },
            title = { Text(stringResource(R.string.go_to_hadith)) },
            text = {
                OutlinedTextField(
                    value = textValue,
                    onValueChange = { if (it.all { char -> char.isDigit() }) textValue = it },
                    label = { Text(stringResource(R.string.enter_hadith_number)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val page = textValue.toIntOrNull()
                        if (page != null && page in 1..totalHadithCount) {
                            scope.launch {
                                pagerState.scrollToPage(page - 1)
                            }
                            showJumpDialog = false
                        } else {
                            Toast.makeText(context, context.getString(R.string.invalid_number), Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showJumpDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

fun shareHadith(context: Context, text: String, description: String) {
    val shareText = "$text\n\n$description"
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Share Hadith"))
}
