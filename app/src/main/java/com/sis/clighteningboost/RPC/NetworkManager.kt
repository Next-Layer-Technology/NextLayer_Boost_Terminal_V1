package com.sis.clighteningboost.RPC

import android.content.Context
import com.sis.clighteningboost.RPC.NetworkManager
import kotlin.Throws
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode
import android.util.Log
import com.sis.clighteningboost.R
import com.sis.clighteningboost.utils.CustomTrustManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

class NetworkManager private constructor() {

    @ApplicationContext lateinit var context: Context
    private lateinit var sslCtx: SSLContext
    private var factory: SSLSocketFactory? = null
    private var socket: SSLSocket? = null

    init {
        try {
            sslCtx = SSLContext.getInstance("SSL")
            val caCertificateInputStream: InputStream = context.resources.openRawResource(R.raw.certificate)
            sslCtx.init(null, arrayOf(CustomTrustManager(caCertificateInputStream)), SecureRandom())

        } catch (e: GeneralSecurityException) {
            Log.e("CLightningApp", Objects.requireNonNull(e.localizedMessage))
        }
    }

    fun connectClient(url: String?, port: Int): Boolean {
        val userMode = -1
        try {
            factory = sslCtx.socketFactory
            socket = factory!!.createSocket(url, port) as SSLSocket
            socket!!.startHandshake()
            out = PrintWriter(
                BufferedWriter(
                    OutputStreamWriter(
                        socket!!.outputStream
                    )
                )
            )
            `in` = BufferedReader(InputStreamReader(socket!!.inputStream))
        } catch (e: Exception) {
            Log.e("CLightningApp", Objects.requireNonNull(e.localizedMessage))
            return false
        }
        return true
    }

    fun validateUser(id: String, psswd: String): Int {
        var userMode = -1
        var response = ""
        try {
            response = recvFromServer()
            if (response.contains("auth,who")) {
                sendToServer("auth,$id,$psswd")
                response = recvFromServer()
                if (response.contains("auth,ok")) {
                    userMode = response.split(",".toRegex()).toTypedArray()[2].toInt()
                } else if (response.contains("auth,fail,-1")) {
                    userMode = response.split(",".toRegex()).toTypedArray()[2].toInt()
                }
            }
        } catch (e: Exception) {
            Log.e("CLightningApp", Objects.requireNonNull(e.localizedMessage))
        }
        return userMode
    }

    @Throws(IOException::class)
    fun recvFromServer(): String {
        val sb = StringBuilder()
        if (`in` != null) {
            var line = ""
            while (`in`!!.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
                if (line.contains("<$#EOT#$>")) break
            }
        } else {
            return ""
        }
        return sb.toString().replace(",<$#EOT#$>", "").replace("\n", "")
    }

    //    public String recvRpcResponse() throws IOException {
    //        StringBuilder sb = new StringBuilder();
    //        if (in != null) {
    //            String line = "";
    //            while ((line = in.readLine()) != null) {
    //                sb.append(line).append("\n");
    //                if(line.contains("<$#EOT#$>"))
    //                    break;
    //            }
    //        } else {
    //            return "";
    //        }
    //        return sb.toString().replace(",<$#EOT#$>", "").replace("\n", "");
    //    }
    /*
            * auth,who,<$#EOT#$>
            auth,admin,admin,<$#EOT#$>
            auth,ok,0,<$#EOT#$>
            db,get-list,images,<$#EOT#$>
            resp,ok,0,response,[ { "UPC" : "069138da-fc90-4b13-b40a-ac06675b06c2"},{ "UPC" : "0d4589ea-198f-47ae-95a7-b186c31e62a6"},{ "UPC" : "16481257-825f-4a06-8014-7c14f600307b"},{ "UPC" : "57ff029e-00a1-48a0-9919-3dadb72f1b57"},{ "UPC" : "a0812988-e76f-4808-928f-275a8e61339c"},{ "UPC" : "b7569980-56a6-41e6-b555-a9c8a34ee687"},{ "UPC" : "ecaa4c9b-51a4-455c-9aa0-41cc731200cc"}],<$#EOT#$>
            db,get-image,images,069138da-fc90-4b13-b40a-ac06675b06c2,<$#EOT#$>
            resp,ok,0,response,[ { "Image" : "89504E470D0A1A0A0000000D494844520000008E0000008E0100000000CF860DA5000000C94944415478DACD96510E843008444938408FE4D57B240E40C2CEC06A5CF71B2221C6BE1F613AB44A3CC3E51D481807528D6FDA8F141FB603A978464C20132C646D3E51CA1812D631897C12A5D0683B1EDAB7A13293DA9FBFDA504539E96786DA90AFED59883353E86614B141CF3A76F4A352191540E86B6B5B113C949D53680E6A3F2A1B696CA4ACDCDA6654910323BE42FBD17760A2FABF5D276DA8166CDBE8E009741E7CB4AFCD214D4BB19429048979EAD931814AE8CAFB79DF86CA4C9E9734B5EE47EFFCE3FB00275EBC0617B451B80000000049454E44AE426082"}],<$#EOT#$>

             */
    @Throws(IOException::class)
    fun sendToServer(msg: String?) {
        if (!msg.isNullOrBlank() && out != null) {
            if (!out!!.checkError()) {
                out!!.println("$msg,<$#EOT#$>")
                out!!.println()
                out!!.flush()
            }
        }
    }

    companion object {
        private var networkManager: NetworkManager? = null
        private var `in`: BufferedReader? = null
        private var out: PrintWriter? = null
        val instance: NetworkManager
            get() {
                if (networkManager == null) {
                    networkManager = NetworkManager()
                    val policy = ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                }
                return networkManager as NetworkManager
            }
    }
}