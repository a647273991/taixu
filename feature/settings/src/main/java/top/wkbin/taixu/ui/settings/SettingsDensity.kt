package top.wkbin.taixu.ui.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Default settings density; system font scaling still applies normally. */
internal object SettingsDensity {
    val rowMinHeight = 68.dp
    val rowHorizontalPadding = 16.dp
    val rowVerticalPadding = 12.dp
    val iconSize = 24.dp
    val iconGap = 12.dp
    val textGap = 4.dp
    val cardPadding = 16.dp
    val sectionGap = 16.dp
}

@Composable
internal fun settingsTitleStyle(): TextStyle = MaterialTheme.typography.titleMedium.copy(
    lineHeight = 22.sp, letterSpacing = 0.sp,
)

@Composable
internal fun settingsSupportingStyle(): TextStyle = MaterialTheme.typography.bodySmall.copy(
    lineHeight = 18.sp, letterSpacing = 0.sp,
)
