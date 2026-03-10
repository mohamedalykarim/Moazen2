package mohalim.islamic.alarm.alert.moazen.ui.more

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.ui.azkar.AzkarActivity
import mohalim.islamic.alarm.alert.moazen.ui.hadith.main.HadithMainActivity
import mohalim.islamic.alarm.alert.moazen.ui.quran.main.QuranMainActivity
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingActivity

@AndroidEntryPoint
class MoreScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MoreScreenUI(
                    onBackClick = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreenUI(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.more),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(R.color.purple_700),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background Pattern
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.transparent_bg),
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Quran Item
                item {
                    MoreItemCard(
                        name = stringResource(R.string.quran),
                        iconId = R.drawable.ic_quran_icon,
                        onClick = {
                            context.startActivity(Intent(context, QuranMainActivity::class.java))
                        }
                    )
                }

                // Hadith Item
                item {
                    MoreItemCard(
                        name = stringResource(R.string.hadith),
                        iconId = R.drawable.ic_hadith_icon,
                        onClick = {
                            context.startActivity(Intent(context, HadithMainActivity::class.java))
                        }
                    )
                }

                // Azkar Item
                item {
                    MoreItemCard(
                        name = stringResource(R.string.azkar),
                        iconId = R.drawable.azkar_icon,
                        onClick = {
                            context.startActivity(Intent(context, AzkarActivity::class.java))
                        }
                    )
                }

                // Setting Item
                item {
                    MoreItemCard(
                        name = stringResource(R.string.setting),
                        iconId = R.drawable.setting_icon,
                        onClick = {
                            context.startActivity(Intent(context, SettingActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MoreItemCard(
    name: String,
    iconId: Int,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = Color.DarkGray,
                contentColor = Color.White
            ) {
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = name,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
