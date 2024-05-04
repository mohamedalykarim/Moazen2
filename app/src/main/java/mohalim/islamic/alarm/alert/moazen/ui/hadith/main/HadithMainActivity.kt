package mohalim.islamic.alarm.alert.moazen.ui.hadith.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.service.FileDownloadWorker
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.core.utils.HadithUtils
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class HadithMainActivity : AppCompatActivity() {
    val viewModel : HadithMainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HadithMainActivityUI(this@HadithMainActivity, viewModel)
        }

    }
}

@Composable
fun HadithMainActivityUI(context: Context, viewModel: HadithMainViewModel){
    val rowaa = arrayOf(stringResource(id = R.string.abi_daud),
        stringResource(id = R.string.ahmed),
        stringResource(id = R.string.bukhari),
        stringResource(id = R.string.darimi),
        stringResource(id = R.string.ibn_maja),
        stringResource(id = R.string.malik),
        stringResource(id = R.string.muslim),
        stringResource(id = R.string.nasai),
        stringResource(id = R.string.trmizi))


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
            items(rowaa.size){index->
                var isPressed by remember { mutableStateOf(false) }
                /** Setting Button **/
                val interactionSetting = remember { MutableInteractionSource() }
                LaunchedEffect(interactionSetting){
                    interactionSetting.interactions.collect{interaction->
                        when(interaction){
                            is PressInteraction.Press ->{
                                isPressed = true
                                Handler(Looper.getMainLooper()).postDelayed({
                                    isPressed = false

                                },90)
                            }
                        }

                    }
                }

                val settingScale by animateFloatAsState(
                    targetValue =  if (isPressed) 0.5f else 1f,
                    animationSpec = tween(durationMillis = 80, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f)),
                    label = "settingScale")

                Column(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .scale(settingScale)
                        .background(Color(android.graphics.Color.parseColor("#fff2f6")))
                        .border(
                            1.dp,
                            Color(android.graphics.Color.parseColor("#ffd4e2")),
                            shape = RoundedCornerShape(5)
                        )
                        .clickable(
                            interactionSource = interactionSetting,
                            indication = null,
                            onClick = {
                                //check if phone storage is full
                                if (!HadithUtils.hasEnoughSpaceForFile(
                                        context,
                                        100 * 1024 * 1024
                                    )
                                ) {
                                    Toast
                                        .makeText(
                                            context,
                                            context.getString(R.string.phone_storage_is_almost_full_please_free_some_space_to_download_the_resources),
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    return@clickable
                                }


                                if (!viewModel.isFileDownloadInProgress(context)) {
                                    runBlocking {
                                        withContext(Dispatchers.IO) {
                                            handleRawyClickButton(
                                                viewModel,
                                                context,
                                                rowaa[index]
                                            )
                                        }

                                        if (!viewModel.isRawyDownloaded(HadithUtils.getFileName(context, rowaa[index]))){
                                            Toast
                                                .makeText(
                                                    context,
                                                    context.resources.getString(R.string.download_resources_is_started),
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    }
                                } else {

                                    Toast
                                        .makeText(
                                            context,
                                            context.resources.getString(R.string.download_in_progress),
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                        )
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                    ){
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.transparent_bg),
                            contentScale = ContentScale.Crop,
                            contentDescription = ""
                        )

                        Row{
                            Image(
                                painterResource(id = R.drawable.zokhrof),
                                contentDescription = "Image",
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(120.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f, true)
                                    .height(70.dp)
                            ) {
                                Box {
                                    Text(text = rowaa[index],
                                        Modifier
                                            .padding(top = 10.dp)
                                            .fillMaxWidth()
                                            .padding(start = 10.dp, end = 10.dp)
                                            .height(50.dp)
                                            .wrapContentHeight(align = Alignment.CenterVertically),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 26.sp
                                    )


                                    Text(text = stringResource(R.string.the_noble_prophet_s_hadith),
                                        Modifier
                                            .padding(top = 50.dp)
                                            .fillMaxWidth()
                                            .padding(start = 10.dp, end = 10.dp)
                                            .height(50.dp)
                                            .wrapContentHeight(align = Alignment.CenterVertically),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(120.dp)
                            ) {
                                Text(text = (index+1).toString(),
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp, end = 10.dp)
                                        .height(120.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                )
                            }


                        }

                    }





                }
            }
        }

    }

}

suspend fun handleRawyClickButton(viewModel: HadithMainViewModel, context: Context, rawy: String) {

    if (!viewModel.isRawyDownloaded(HadithUtils.getFileName(context, rawy))){
        val url = HadithUtils.getFileURL(context, rawy)
        val fileName = HadithUtils.getFileName(context, rawy)+".json"

        val data = Data.Builder()
            .putString("URL", url)
            .putString("FILE_NAME", fileName)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val worker = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setInputData(data)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(constraints)
            .setId(UUID.fromString(Constants.DOWNLOAD_WORKER_MANAGER_UUID))
            .build()

        WorkManager.getInstance(context)
            .enqueue(worker)

    }else{
        viewModel.startHadithViewerActivity(context, rawy)
    }


}
