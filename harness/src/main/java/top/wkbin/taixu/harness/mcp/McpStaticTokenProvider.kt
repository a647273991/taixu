package top.wkbin.taixu.harness.mcp

/**
 * 静态 Bearer token 提供方：返回用户为指定 MCP 服务配置的 token（明文，解密后）。
 * 由 core:datastore 的 McpStaticTokenStore 实现，避免 harness 直接依赖存储细节。
 */
fun interface McpStaticTokenProvider {
    suspend fun bearerFor(serverId: String): String?
}
