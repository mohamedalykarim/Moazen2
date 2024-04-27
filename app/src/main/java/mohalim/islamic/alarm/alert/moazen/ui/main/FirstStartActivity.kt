package mohalim.islamic.alarm.alert.moazen.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color.parseColor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.ui.compose.SummerTime
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingButton
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class FirstStartActivity : AppCompatActivity() {
    private val viewModel: FirstStartViewModel by viewModels()
    val PERMISSION_REQUEST_CODE = 112
    val manufacturer = Build.MANUFACTURER.lowercase(Locale.ROOT)


    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FirstStartActivityUI(this, viewModel)
        }

        viewModel.addDefaultAzkar()

    }

    override fun onResume() {
        super.onResume()

        viewModel.observeSummerTime()
        viewModel.observeIsFirstTimeOpen()

        if (isAutoStartPermissionGranted(this)){
            viewModel.setAutoStartPermissionGranted(true)
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            viewModel.setNotificationPermissionGranted(true)
        }
    }

    private val requestPermissionLauncherForNotificiationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
            if (isGranted) {
                viewModel.setNotificationPermissionGranted(true)
            } else {
                Toast.makeText(this,
                    getString(R.string.you_refused_to_grant_the_permission_please_grant_it_from_the_setting_so_you_can_get_the_application_notification), Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)

            }
        }

    private fun getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {

                when {
                    ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                        Log.d("TAG", "getNotificationPermission: 1")

                    }
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS) -> {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)

                    }
                    else -> {
                        Log.d("TAG", "getNotificationPermission: 3")

                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        requestPermissionLauncherForNotificiationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun getScheduleAlarmPermission() {
        try {
            if (Build.VERSION.SDK_INT > 33) {

                val alarmManager = ContextCompat.getSystemService(this@FirstStartActivity, AlarmManager::class.java)
                if (alarmManager?.canScheduleExactAlarms() == false) {
                    Intent().also { intent ->
                        intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                        startActivity(intent)
                    }
                }else{
                    viewModel.setScheduleAlarmPermissionGranted(true)
                }
            }
        } catch (_: Exception) {
        }
    }


    fun isAutoStartPermissionGranted(context: Context): Boolean {
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

        val summerTimeState by viewModel.summerTimeState.collectAsState()
        val isFirstTimeOpen by viewModel.isFirstTimeOpen.collectAsState()

        val autoStartPermissionGranted by viewModel.autoStartPermissionGranted.collectAsState()
        val notificationPermissionGranted by viewModel.notificationPermissionGranted.collectAsState()
        val scheduleAlarmPermissionGranted by viewModel.scheduleAlarmPermissionGranted.collectAsState()

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


                /**
                 * Summer Time
                 */

                if (isFirstTimeOpen){
                    SummerTime(context, dataStore = dataStore, summerTimeState = summerTimeState)
                }


                /**
                 * Auto Start Permission
                 */
                if(manufacturer.contains("xiaomi") || manufacturer.contains("oppo") || manufacturer.contains("vivo")
                    || manufacturer.contains("letv") || manufacturer.contains("leeco") || manufacturer.contains("huawei")){


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
                            text = stringResource(R.string.you_should_grant_auto_start_permission_for_the_application_so_it_can_launch_after_rebooting_system),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )

                        if (autoStartPermissionGranted){
                            Text(
                                text = stringResource(R.string.permission_granted),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                color = Color(parseColor("#007400")),
                                fontWeight = FontWeight.Bold
                            )
                        }else{
                            val not_required_for_all_phones = stringResource(R.string.not_required_for_all_phones)

                            SettingButton(name = stringResource(R.string.auto_start_permission), iconId = R.drawable.ic_masjed_icon, onClickCard = {
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
                                    Toast.makeText(context,
                                        not_required_for_all_phones, Toast.LENGTH_LONG).show()
                                }
                            } )
                        }


                    }

                    Spacer(modifier = Modifier.height(16.dp))




                }




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
                        text = stringResource(R.string.you_should_grant_notification_permission_to_get_app_notifications),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )

                    if (notificationPermissionGranted){
                        Text(
                            text = stringResource(id = R.string.permission_granted),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            color = Color(parseColor("#007400")),
                            fontWeight = FontWeight.Bold
                        )
                    }else{
                        SettingButton(name = stringResource(R.string.notification_permission), iconId = R.drawable.ic_masjed_icon, onClickCard = {
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


                if (Build.VERSION.SDK_INT > 33) {

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
                            text = stringResource(R.string.you_should_grant_alarm_permission),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )

                        if (scheduleAlarmPermissionGranted){
                            Text(
                                text = stringResource(id = R.string.permission_granted),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                color = Color(parseColor("#007400")),
                                fontWeight = FontWeight.Bold
                            )
                        }else{
                            SettingButton(name = stringResource(R.string.schedule_alarm_permission), iconId = R.drawable.ic_masjed_icon, onClickCard = {
                                if (Build.VERSION.SDK_INT > 33) {
                                        getScheduleAlarmPermission();
                                }
                            } )
                        }





                    }

                }






                }
        }
    }

}