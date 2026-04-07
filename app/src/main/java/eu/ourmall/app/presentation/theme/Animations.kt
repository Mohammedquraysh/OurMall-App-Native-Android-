package eu.ourmall.app.presentation.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer

/** Screen transition specs **/
fun slideInFromRight(): EnterTransition =
    slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(320, easing = FastOutSlowInEasing)
    ) + fadeIn(tween(320))

fun slideOutToLeft(): ExitTransition =
    slideOutHorizontally(
        targetOffsetX = { -it / 3 },
        animationSpec = tween(320, easing = FastOutSlowInEasing)
    ) + fadeOut(tween(200))

fun slideInFromLeft(): EnterTransition =
    slideInHorizontally(
        initialOffsetX = { -it / 3 },
        animationSpec = tween(320, easing = FastOutSlowInEasing)
    ) + fadeIn(tween(320))

fun slideOutToRight(): ExitTransition =
    slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(320, easing = FastOutSlowInEasing)
    ) + fadeOut(tween(200))

fun scaleInFade(): EnterTransition =
    scaleIn(initialScale = 0.92f, animationSpec = tween(300)) + fadeIn(tween(300))

fun scaleOutFade(): ExitTransition =
    scaleOut(targetScale = 0.92f, animationSpec = tween(200)) + fadeOut(tween(200))

/** Shimmer loading effect **/
@Composable
fun shimmerBrush(): androidx.compose.ui.graphics.Brush {
    val shimmerColors = listOf(
        androidx.compose.ui.graphics.Color.LightGray.copy(alpha = 0.6f),
        androidx.compose.ui.graphics.Color.LightGray.copy(alpha = 0.2f),
        androidx.compose.ui.graphics.Color.LightGray.copy(alpha = 0.6f),
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    return androidx.compose.ui.graphics.Brush.linearGradient(
        colors = shimmerColors,
        start = androidx.compose.ui.geometry.Offset(translateAnim - 200f, 0f),
        end = androidx.compose.ui.geometry.Offset(translateAnim, 0f),
    )
}

/** Bounce scale for button press **/
@Composable
fun Modifier.bounceClick(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "bounce"
    )
    return this
        .scale(scale)
        .then(
            Modifier.graphicsLayer {}
        )
}

/** Count-up number animation **/
@Composable
fun animatedDouble(
    target: Double,
    durationMs: Int = 600,
): State<Double> {
    val animatable = remember { Animatable(target.toFloat()) }
    LaunchedEffect(target) {
        animatable.animateTo(
            targetValue = target.toFloat(),
            animationSpec = tween(durationMs, easing = FastOutSlowInEasing)
        )
    }
    return derivedStateOf { animatable.value.toDouble() } as State<Double>
}

/** Staggered list item entrance **/
@Composable
fun AnimatedListItem(
    index: Int,
    visible: Boolean = true,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300, delayMillis = (index * 60).coerceAtMost(400))) +
                slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(300, delayMillis = (index * 60).coerceAtMost(400), easing = FastOutSlowInEasing)
                ),
        exit = fadeOut(tween(150)) + slideOutVertically()
    ) {
        content()
    }
}

/** Pulsing dot for live indicators **/
@Composable
fun PulsingDot(color: androidx.compose.ui.graphics.Color) {
    val inf = rememberInfiniteTransition(label = "pulse")
    val scale by inf.animateFloat(
        initialValue = 0.8f, targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            tween(800, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val alpha by inf.animateFloat(
        initialValue = 0.5f, targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            tween(800, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    Box(
        Modifier
            .scale(scale)
            .graphicsLayer { this.alpha = alpha }
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier) {
            drawCircle(color = color, radius = 5f)
        }
    }
}
