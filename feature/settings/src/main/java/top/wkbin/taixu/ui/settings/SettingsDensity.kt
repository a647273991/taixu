package top.wkbin.taixu.ui.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Settings density that can be tuned at runtime.
 *
 * The tunable fields are Compose observable state, so overriding them via [applyTuning]
 * recomposes every settings screen immediately -- no rebuild or reinstall required.
 * Values are persisted through `UiDensityPreferences` and re-applied on screen entry.
 * System font scaling still applies normally.
 */
internal object SettingsDensity {
    var rowMinHeight by mutableStateOf(68.dp)
        private set
    var rowHorizontalPadding by mutableStateOf(16.dp)
        private set
    var rowVerticalPadding by mutableStateOf(12.dp)
        private set
    var sectionGap by mutableStateOf(16.dp)
        private set

    val iconSize = 24.dp
    val iconGap = 12.dp
    val textGap = 4.dp
    val cardPadding = 16.dp

    /** Overrides the tunable density values in place; triggers recomposition. */
    fun applyTuning(
        rowMinHeightDp: Int,
        rowHorizontalPaddingDp: Int,
        rowVerticalPaddingDp: Int,
        sectionGapDp: Int,
    ) {
        rowMinHeight = rowMinHeightDp.dp
        rowHorizontalPadding = rowHorizontalPaddingDp.dp
        rowVerticalPadding = rowVerticalPaddingDp.dp
        sectionGap = sectionGapDp.dp
    }
}

@Composable
internal fun settingsTitleStyle(): TextStyle = MaterialTheme.typography.titleMedium.copy(
    lineHeight = 22.sp, letterSpacing = 0.sp,
)

@Composable
internal fun settingsSupportingStyle(): TextStyle = MaterialTheme.typography.bodySmall.copy(
    lineHeight = 18.sp, letterSpacing = 0.sp,
)
