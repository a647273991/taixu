package top.wkbin.taixu.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import top.wkbin.taixu.core.datastore.ROW_HORIZONTAL_PADDING_RANGE
import top.wkbin.taixu.core.datastore.ROW_MIN_HEIGHT_RANGE
import top.wkbin.taixu.core.datastore.SECTION_GAP_RANGE
import top.wkbin.taixu.core.datastore.UiDensityPreferences
import top.wkbin.taixu.core.datastore.UiDensityValues
import kotlin.math.roundToInt

private val CompactDensity = UiDensityValues(
    rowMinHeightDp = 60, rowHorizontalPaddingDp = 14, rowVerticalPaddingDp = 10, sectionGapDp = 12,
)
private val StandardDensity = UiDensityValues(
    rowMinHeightDp = 68, rowHorizontalPaddingDp = 16, rowVerticalPaddingDp = 12, sectionGapDp = 16,
)
private val LooseDensity = UiDensityValues(
    rowMinHeightDp = 80, rowHorizontalPaddingDp = 20, rowVerticalPaddingDp = 14, sectionGapDp = 20,
)

private fun UiDensityValues.applyToUi() = SettingsDensity.applyTuning(
    rowMinHeightDp = rowMinHeightDp,
    rowHorizontalPaddingDp = rowHorizontalPaddingDp,
    rowVerticalPaddingDp = rowVerticalPaddingDp,
    sectionGapDp = sectionGapDp,
)

/**
 * Re-applies the persisted density on settings entry. Without this, every cold start would
 * fall back to the compiled defaults until the user touches the tuning section.
 */
@Composable
internal fun ApplyUiDensityPreference() {
    val context = LocalContext.current
    val preferences = remember(context) { UiDensityPreferences(context) }
    val density by preferences.values.collectAsStateWithLifecycle(initialValue = UiDensityValues())
    LaunchedEffect(density) { density.applyToUi() }
}

/**
 * Runtime density tuning. Dragging only repaints (in-memory override); the value is persisted
 * once the gesture ends, so the whole section stays smooth.
 */
@Composable
internal fun UiDensityTuningSection() {
    val context = LocalContext.current
    val preferences = remember(context) { UiDensityPreferences(context) }
    val density by preferences.values.collectAsStateWithLifecycle(initialValue = UiDensityValues())
    val scope = rememberCoroutineScope()

    fun commit(next: UiDensityValues) {
        next.applyToUi()
        scope.launch { preferences.save(next) }
    }

    SettingsGroup {
        Column(
            Modifier.fillMaxWidth().padding(SettingsDensity.cardPadding),
            verticalArrangement = Arrangement.spacedBy(SettingsDensity.textGap),
        ) {
            Text("界面密度", style = MaterialTheme.typography.titleMedium)
            Text(
                "仅影响设置页。拖动即时生效并自动保存，无需重新安装。",
                style = settingsSupportingStyle(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = { commit(CompactDensity) }) { Text("紧凑") }
                TextButton(onClick = { commit(StandardDensity) }) { Text("标准") }
                TextButton(onClick = { commit(LooseDensity) }) { Text("宽松") }
            }
            DensitySlider(
                label = "行高",
                value = density.rowMinHeightDp,
                range = ROW_MIN_HEIGHT_RANGE,
                onPreview = { density.copy(rowMinHeightDp = it).applyToUi() },
                onCommit = { commit(density.copy(rowMinHeightDp = it)) },
            )
            DensitySlider(
                label = "横向内边距",
                value = density.rowHorizontalPaddingDp,
                range = ROW_HORIZONTAL_PADDING_RANGE,
                onPreview = { density.copy(rowHorizontalPaddingDp = it).applyToUi() },
                onCommit = { commit(density.copy(rowHorizontalPaddingDp = it)) },
            )
            DensitySlider(
                label = "分组间距",
                value = density.sectionGapDp,
                range = SECTION_GAP_RANGE,
                onPreview = { density.copy(sectionGapDp = it).applyToUi() },
                onCommit = { commit(density.copy(sectionGapDp = it)) },
            )
        }
    }
}

@Composable
private fun DensitySlider(
    label: String,
    value: Int,
    range: IntRange,
    onPreview: (Int) -> Unit,
    onCommit: (Int) -> Unit,
) {
    var dragValue by remember(value) { mutableFloatStateOf(value.toFloat()) }
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(
                "${dragValue.roundToInt()}dp",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Slider(
            value = dragValue,
            onValueChange = { raw ->
                dragValue = raw
                onPreview(raw.roundToInt().coerceIn(range))
            },
            onValueChangeFinished = { onCommit(dragValue.roundToInt().coerceIn(range)) },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = (range.last - range.first - 1).coerceAtLeast(0),
        )
    }
}
