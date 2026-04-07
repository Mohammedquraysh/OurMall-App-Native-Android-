package eu.ourmall.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Brand Colors **/
val OurMallOrange   = Color(0xFFFF6B2C)
val OurMallOrangeDim= Color(0xFFFF8C57)
val OurMallDark     = Color(0xFF0F1117)
val OurMallSurface  = Color(0xFF1A1D27)
val OurMallCard     = Color(0xFF222637)
val OurMallBorder   = Color(0xFF2E3348)
val OurMallText     = Color(0xFFF2F4FF)
val OurMallSubtext  = Color(0xFF8A8FA8)
val OurMallGreen    = Color(0xFF2ECC71)
val OurMallRed      = Color(0xFFE74C3C)
val OurMallAmber    = Color(0xFFF39C12)
val OurMallBlue     = Color(0xFF3498DB)

/** Light colors **/
val OurMallLightBg      = Color(0xFFF5F7FF)
val OurMallLightSurface = Color(0xFFFFFFFF)
val OurMallLightCard    = Color(0xFFFFFFFF)
val OurMallLightBorder  = Color(0xFFE0E4F0)
val OurMallLightText    = Color(0xFF0F1117)
val OurMallLightSubtext = Color(0xFF6B7280)

private val DarkColorScheme = darkColorScheme(
    primary         = OurMallOrange,
    onPrimary       = Color.White,
    primaryContainer= Color(0xFF3D1A00),
    secondary       = OurMallBlue,
    background      = OurMallDark,
    surface         = OurMallSurface,
    surfaceVariant  = OurMallCard,
    onBackground    = OurMallText,
    onSurface       = OurMallText,
    onSurfaceVariant= OurMallSubtext,
    outline         = OurMallBorder,
    error           = OurMallRed,
)

private val LightColorScheme = lightColorScheme(
    primary         = OurMallOrange,
    onPrimary       = Color.White,
    primaryContainer= Color(0xFFFFE5D6),
    secondary       = OurMallBlue,
    background      = OurMallLightBg,
    surface         = OurMallLightSurface,
    surfaceVariant  = OurMallLightCard,
    onBackground    = OurMallLightText,
    onSurface       = OurMallLightText,
    onSurfaceVariant= OurMallLightSubtext,
    outline         = OurMallLightBorder,
    error           = OurMallRed,
)

val AppShapes = Shapes(
    small  = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    large  = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
)

@Composable
fun OurMallTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colors,
        shapes = AppShapes,
        content = content
    )
}
