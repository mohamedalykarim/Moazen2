package mohalim.islamic.alarm.alert.moazen.ui.main

import android.content.Context
import android.graphics.Color.parseColor
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = PreferencesUtils(this)
        lifecycleScope.launch {
            pref.getIsFirstOpen().collect{
                if (it){
                    Log.d("TAG", "onCreate: first open")
                    viewModel.setShowCityBottomSheet(true)
                }
            }
        }

        setContent {
            MainActivityUi(context = this@MainActivity, viewModel)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityUi (context: Context, viewModel : MainActivityViewModel){




    val showCityBottomSheet by viewModel.showCityBottomSheet.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()



    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(parseColor("#ffffff")))
    ){
        Image(modifier = Modifier.fillMaxSize(), painter = painterResource(id = R.drawable.transparent_bg), contentScale = ContentScale.Crop, contentDescription = "")
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.top),
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )

            Column {
                Text(modifier = Modifier.fillMaxWidth(), text = "Till Next pray:", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, textAlign = TextAlign.Center)
                Text(modifier = Modifier.fillMaxWidth(), text = "00:00:00", fontWeight = FontWeight.ExtraBold, fontSize = 40.sp, textAlign = TextAlign.Center)
            }

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, false),
                painter = painterResource(id = R.drawable.bottom),
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
        }


        if (showCityBottomSheet) {
            val cities : MutableList<String>  = ArrayList()
            cities.add("Luxor")
            cities.add("Higaza")


            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.setShowCityBottomSheet(false)
                },
                sheetState = sheetState
            ) {

                



                Text("Choose Current City")




                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}