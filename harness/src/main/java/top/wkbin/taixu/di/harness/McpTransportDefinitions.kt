package top.wkbin.taixu.di.harness

import android.content.Context
import org.koin.core.module.Module
import top.wkbin.taixu.core.datastore.McpStaticTokenStore
import top.wkbin.taixu.harness.mcp.McpHttpTransport
import top.wkbin.taixu.harness.mcp.McpStaticTokenProvider
import java.io.File

/** 远程 MCP 传输层注册：含用户配置的静态 Bearer token 提供方。 */
fun Module.mcpTransportDefinitions() {
    single { McpStaticTokenStore(get<Context>()) }

    single<McpHttpTransport> {
        McpHttpTransport(
            client = get(),
            json = get(),
            logger = get(),
            oauthTokens = get(),
            spillDirectory = File(get<Context>().cacheDir, "taixu_mcp_spills"),
            staticTokens = McpStaticTokenProvider { serverId ->
                get<McpStaticTokenStore>().token(serverId)
            },
        )
    }
}
