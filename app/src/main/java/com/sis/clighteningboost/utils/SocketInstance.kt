package com.sis.clighteningboost.utils

import android.app.Application
import io.socket.client.IO
import io.socket.client.Socket
import java.lang.RuntimeException
import java.net.URISyntaxException
import java.util.*

class SocketInstance(accessToken: String) : Application() {

    var socket: Socket? = null

    init {
        val options = IO.Options()
        val headers: MutableMap<String, List<String>> = HashMap()
        val bearer = "Bearer $accessToken"
        headers["Authorization"] = Arrays.asList(bearer)
        options.extraHeaders = headers
        try {
            socket = IO.socket("https://realtime.nextlayer.live", options)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }


}