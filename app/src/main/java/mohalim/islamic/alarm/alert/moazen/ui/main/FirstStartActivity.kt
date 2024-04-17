package mohalim.islamic.alarm.alert.moazen.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color.parseColor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingButton
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class FirstStartActivity : AppCompatActivity() {
    private val viewModel: FirstStartViewModel by viewModels()
    val PERMISSION_REQUEST_CODE = 112


    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FirstStartActivityUI(this, viewModel)
        }

    }

    override fun onResume() {
        super.onResume()
        if (isAutoStartPermissionGranted(this)){
            viewModel.setAutoStartPermissionGranted(true)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.setNotificationPermissionGranted(true)

            } else {
                Toast.makeText(this, "You refused to grant the permission, please grant it from the setting so you can get the application notification", Toast.LENGTH_SHORT).show()
            }
        }

    private fun getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {

                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.
                    }
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.POST_NOTIFICATIONS) -> {
                        // In an educational UI, explain to the user why your app requires this
                        // permission for a specific feature to behave as expected, and what
                        // features are disabled if it's declined. In this UI, include a
                        // "cancel" or "no thanks" button that lets the user continue
                        // using your app without granting the permission.
                    }
                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        requestPermissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
        } catch (_: Exception) {
        }
    }


    fun isAutoStartPermissionGranted(context: Context): Boolean {
        val manufacturer = Build.MANUFACTURER.lowercase(Locale.ROOT)
        return when {
            manufacturer.contains("xiaomi") -> isXiaomiAutoStartPermissionGranted(context)
            manufacturer.contains("oppo") -> isOppoAutoStartPermissionGranted(context)
            manufacturer.contains("vivo") -> isVivoAutoStartPermissionGranted(context)
            manufacturer.contains("letv") || manufacturer.contains("leeco") -> isLetvAutoStartPermissionGranted(context)
            manufacturer.contains("huawei") -> isHuaweiAutoStartPermissionGranted(context)
            else -> true // For other devices, assume the permission is granted
        }
    }

    fun isXiaomiAutoStartPermissionGranted(context: Context): Boolean {
        val componentName = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
        Log.d("TAG", "isXiaomiAutoStartPermissionGranted: "+isComponentEnabled(context, componentName))
        return isComponentEnabled(context, componentName)
    }

    fun isOppoAutoStartPermissionGranted(context: Context): Boolean {
        val componentName = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")
        return isComponentEnabled(context, componentName)
    }

    fun isVivoAutoStartPermissionGranted(context: Context): Boolean {
        val componentName = ComponentName.unflattenFromString("com.iqoo.secure/.ui.phoneoptimize.AddWhiteListActivity")
        return isComponentEnabled(context, componentName!!)
    }

    fun isLetvAutoStartPermissionGranted(context: Context): Boolean {
        val componentName = ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")
        return isComponentEnabled(context, componentName)
    }

    fun isHuaweiAutoStartPermissionGranted(context: Context): Boolean {
        val componentName = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")
        return isComponentEnabled(context, componentName)
    }

    private fun isComponentEnabled(context: Context, componentName: ComponentName): Boolean {

        val intent = Intent()
        intent.component = componentName
        val list = packageManager.queryBroadcastReceivers(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0

    }

    @Composable
    fun FirstStartActivityUI(
        context: Context,
        viewModel: FirstStartViewModel
    ) {

        val autoStartPermissionGranted by viewModel.autoStartPermissionGranted.collectAsState()
        val notificationPermissionGranted by viewModel.notificationPermissionGranted.collectAsState()

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
            Column(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color(android.graphics.Color.parseColor("#fff2f6")))
                        .border(
                            1.dp,
                            Color(android.graphics.Color.parseColor("#ffd4e2")),
                            shape = RoundedCornerShape(5)
                        )

                ) {

                    Text(
                        text = "You should grant auto start permission for the application so it can launch after rebooting system",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )

                    if (autoStartPermissionGranted){
                        Text(
                            text = "Permission Granted",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            color = Color(parseColor("#007400")),
                            fontWeight = FontWeight.Bold
                        )
                    }else{
                        SettingButton(name = "Auto Start permission", iconId = R.drawable.ic_masjed_icon, onClickCard = {
                            val intent = Intent()
                            if ("xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)) {
                                intent.setComponent(
                                    ComponentName(
                                        "com.miui.securitycenter",
                                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                                    )
                                )
                                startActivity(intent)

                            } else if ("oppo".equals(Build.MANUFACTURER, ignoreCase = true)) {
                                intent.setComponent(
                                    ComponentName(
                                        "com.coloros.safecenter",
                                        "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                                    )
                                )
                                startActivity(intent)

                            } else if ("vivo".equals(Build.MANUFACTURER, ignoreCase = true)) {
                                intent.setComponent(
                                    ComponentName(
                                        "com.vivo.permissionmanager",
                                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                                    )
                                )
                                startActivity(intent)

                            } else if ("Letv".equals(Build.MANUFACTURER, ignoreCase = true)) {
                                intent.setComponent(
                                    ComponentName(
                                        "com.letv.android.letvsafe",
                                        "com.letv.android.letvsafe.AutobootManageActivity"
                                    )
                                )
                                startActivity(intent)

                            } else if ("Honor".equals(Build.MANUFACTURER, ignoreCase = true)) {
                                intent.setComponent(
                                    ComponentName(
                                        "com.huawei.systemmanager",
                                        "com.huawei.systemmanager.optimize.process.ProtectActivity"
                                    )
                                )
                                startActivity(intent)

                            }else{
                                Toast.makeText(context, "Not Required for all Phones", Toast.LENGTH_LONG).show()
                            }
                        } )
                    }


                }

                Spacer(modifier = Modifier.height(16.dp))


                /**
                 * Notification permission
                 */
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color(android.graphics.Color.parseColor("#fff2f6")))
                        .border(
                            1.dp,
                            Color(android.graphics.Color.parseColor("#ffd4e2")),
                            shape = RoundedCornerShape(5)
                        )

                ) {

                    Text(
                        text = "You should grant notification permission to get app notifications",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )

                    if (notificationPermissionGranted){
                        Text(
                            text = "Permission Granted",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            color = Color(parseColor("#007400")),
                            fontWeight = FontWeight.Bold
                        )
                    }else{
                        SettingButton(name = "Notification Permission", iconId = R.drawable.ic_masjed_icon, onClickCard = {
                            if (Build.VERSION.SDK_INT > 32) {
                                if (!shouldShowRequestPermissionRationale("112")){
                                    getNotificationPermission();
                                }
                            }else{
                                viewModel.setNotificationPermissionGranted(true)
                            }
                        } )
                    }





                }




            }
        }
    }

}