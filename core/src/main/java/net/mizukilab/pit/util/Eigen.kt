package net.mizukilab.pit.util

import cn.charlotte.pit.ThePit
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Araykal
 * @since 2025/7/28
 */
object Eigen {
    private fun getAddress(): String? {
        val ipServiceUrl = "https://checkip.amazonaws.com"
        return try {
            val url = URL(ipServiceUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            connection.inputStream.bufferedReader().use { it.readLine() }
        } catch (e: Exception) {
            null
        }
    }

    fun isUserByIP(ip: String): Boolean {
        return ip == getAddress() && getAddress() != null
    }

    fun isUser(user: User): Boolean {
        return ThePit.getInstance().globalConfig.token == user.name
    }

    enum class User() {
        Clover, SmallMY
    }
}