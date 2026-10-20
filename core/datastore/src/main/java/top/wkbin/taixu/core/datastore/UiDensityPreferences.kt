package top.wkbin.taixu.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map

/** Tunable settings-screen density values, all expressed in dp. */
data class UiDensityValues(
    val rowMinHeightDp: Int = DEFAULT_ROW_MIN_HEIGHT_DP,
    val rowHorizontalPaddingDp: Int = DEFAULT_ROW_HORIZONTAL_PADDING_DP,
    val rowVerticalPaddingDp: Int = DEFAULT_ROW_VERTICAL_PADDING_DP,
    val sectionGapDp: Int = DEFAULT_SECTION_GAP_DP,
)

const val DEFAULT_ROW_MIN_HEIGHT_DP = 68
const val DEFAULT_ROW_HORIZONTAL_PADDING_DP = 16
const val DEFAULT_ROW_VERTICAL_PADDING_DP = 12
const val DEFAULT_SECTION_GAP_DP = 16

/** Allowed ranges, shared by the persistence layer and the tuning UI. */
val ROW_MIN_HEIGHT_RANGE = 56..88
val ROW_HORIZONTAL_PADDING_RANGE = 12..24
val ROW_VERTICAL_PADDING_RANGE = 8..20
val SECTION_GAP_RANGE = 8..24

/** Persists the tunable settings density so a choice survives app restarts. */
class UiDensityPreferences(private val dataStore: DataStore<Preferences>) {
    constructor(context: Context) : this(context.applicationContext.settingsDataStore)

    val values = dataStore.data.map { prefs ->
        UiDensityValues(
            rowMinHeightDp = (prefs[ROW_MIN_HEIGHT] ?: DEFAULT_ROW_MIN_HEIGHT_DP).coerceIn(ROW_MIN_HEIGHT_RANGE),
            rowHorizontalPaddingDp = (prefs[ROW_HORIZONTAL_PADDING] ?: DEFAULT_ROW_HORIZONTAL_PADDING_DP)
                .coerceIn(ROW_HORIZONTAL_PADDING_RANGE),
            rowVerticalPaddingDp = (prefs[ROW_VERTICAL_PADDING] ?: DEFAULT_ROW_VERTICAL_PADDING_DP)
                .coerceIn(ROW_VERTICAL_PADDING_RANGE),
            sectionGapDp = (prefs[SECTION_GAP] ?: DEFAULT_SECTION_GAP_DP).coerceIn(SECTION_GAP_RANGE),
        )
    }

    suspend fun save(density: UiDensityValues) {
        dataStore.edit { prefs ->
            prefs[ROW_MIN_HEIGHT] = density.rowMinHeightDp.coerceIn(ROW_MIN_HEIGHT_RANGE)
            prefs[ROW_HORIZONTAL_PADDING] = density.rowHorizontalPaddingDp.coerceIn(ROW_HORIZONTAL_PADDING_RANGE)
            prefs[ROW_VERTICAL_PADDING] = density.rowVerticalPaddingDp.coerceIn(ROW_VERTICAL_PADDING_RANGE)
            prefs[SECTION_GAP] = density.sectionGapDp.coerceIn(SECTION_GAP_RANGE)
        }
    }

    private companion object {
        val ROW_MIN_HEIGHT = intPreferencesKey("ui_density_row_min_height_dp")
        val ROW_HORIZONTAL_PADDING = intPreferencesKey("ui_density_row_horizontal_padding_dp")
        val ROW_VERTICAL_PADDING = intPreferencesKey("ui_density_row_vertical_padding_dp")
        val SECTION_GAP = intPreferencesKey("ui_density_section_gap_dp")
    }
}
