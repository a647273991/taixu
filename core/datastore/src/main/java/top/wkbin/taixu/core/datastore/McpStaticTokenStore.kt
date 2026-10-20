package top.wkbin.taixu.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import top.wkbin.taixu.core.security.SecretManager

/**
 * 远程 MCP 服务的静态 Bearer token 存储。
 *
 * token 用 Android Keystore 派生的 AES-GCM 密钥加密后再落到 settings DataStore，
 * 不进入 Room、不随 MCP 配置导出，避免明文泄漏。
 */
class McpStaticTokenStore(
    private val dataStore: DataStore<Preferences>,
    private val secretManager: SecretManager,
) {
    constructor(context: Context) : this(
        context.applicationContext.settingsDataStore,
        SecretManager(),
    )

    /** 保存（或清除，传入空白时）指定服务的 token。 */
    suspend fun save(serverId: String, token: String) {
        if (serverId.isBlank()) return
        dataStore.edit { prefs ->
            if (token.isBlank()) {
                prefs.remove(keyOf(serverId))
            } else {
                prefs[keyOf(serverId)] = secretManager.encrypt(token.trim())
            }
        }
    }

    /** 读取解密后的 token；无配置或解密失败时返回空串。 */
    suspend fun token(serverId: String): String {
        if (serverId.isBlank()) return ""
        val encrypted = dataStore.data.first()[keyOf(serverId)]
        if (encrypted.isNullOrBlank()) return ""
        return secretManager.decrypt(encrypted).orEmpty()
    }

    suspend fun hasToken(serverId: String): Boolean = token(serverId).isNotBlank()

    suspend fun clear(serverId: String) = save(serverId, "")

    private fun keyOf(serverId: String) = stringPreferencesKey("mcp_static_bearer_$serverId")
}
