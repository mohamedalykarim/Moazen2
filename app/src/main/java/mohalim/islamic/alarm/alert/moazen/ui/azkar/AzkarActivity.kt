package mohalim.islamic.alarm.alert.moazen.ui.azkar

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.vectorResource

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R

@AndroidEntryPoint
class AzkarActivity : AppCompatActivity() {
    private val viewModel : AzkarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AzkarActivityUI(this@AzkarActivity, viewModel)
        }
    }
}

@Composable
fun AzkarActivityUI(context: Context, viewModel: AzkarViewModel) {
    var isAnimating by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition()
    val animatedValue1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedValue2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Create Painters for different parts inside the circle (Replace with your own vector drawables)
    val painter1: Painter = painterResource(id = R.drawable.azkar_animated_drawable)
    val painter2: Painter = painterResource(id = R.drawable.azkar_animated_drawable)

    // Composable button to trigger the animations
    Button(onClick = { isAnimating = !isAnimating }) {
        Image(
            painter = if (isAnimating) painter1 else painter2,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }

}