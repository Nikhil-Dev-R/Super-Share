package com.rudraksha.supershare.core.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Picker : Screen("picker")
    object SenderWaiting : Screen("sender_waiting")
    object ReceiverWaiting : Screen("receiver_waiting")
    object Transfer : Screen("transfer")
    object History : Screen("history")
    object Settings : Screen("settings")
    object TransmissionMode : Screen("transmission_mode")
    object Discovery: Screen("discovery")
//    object ConnectReceiver : SerializableRoute<Mode>(
//        baseRoute = "connect_receiver",
//        argKey = "mode",
//        serializer = Mode,
//    )
//        fun createRoute(mode: Mode) = "connect_receiver/${Json.encodeToString(mode)}"

    open class SerializableRoute<T>(
        val baseRoute: String,
        val argKey: String = "data",
        val serializer: KSerializer<T>
    ) : Screen("$baseRoute/{$argKey}") {

        fun buildRoute(data: T): String {
            val json = Json.encodeToString(serializer, data)
            val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$encoded"
        }

        fun parseArgument(encoded: String): T? {
            return try {
                val decoded = URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
                Json.decodeFromString(serializer, decoded)
            } catch (e: Exception) {
                null
            }
        }
    }
}
