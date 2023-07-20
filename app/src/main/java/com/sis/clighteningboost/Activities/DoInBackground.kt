package com.sis.clighteningboost.Activities

import android.app.Activity
import com.sis.clighteningboost.Interface.RPCResponse
import android.os.AsyncTask
import android.app.ProgressDialog
import android.os.Build
import android.util.Log
import com.sis.clighteningboost.RPC.NetworkManager
import com.sis.clighteningboost.utils.StaticClass
import com.sis.clighteningboost.utils.Utils
import java.lang.Boolean
import java.lang.Exception

internal class DoInBackground(
    var parent: Activity,
    rpcResponse: RPCResponse,
    responseType: Int,
    message: String?
) : AsyncTask<String?, String?, String>() {
    var progressDialog: ProgressDialog
    var st: StaticClass
    var TAG = "tag"
    var rpcResponse: RPCResponse
    var responseType: Int
    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog.show()
    }

    init {
        st = StaticClass(parent)
        progressDialog = ProgressDialog(parent)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        this.rpcResponse = rpcResponse
        this.responseType = responseType
    }

    fun initExecute(array: Array<String?>) {
        if (Build.VERSION.SDK_INT >= 11 /*HONEYCOMB*/) {
            executeOnExecutor(THREAD_POOL_EXECUTOR, *array)
        } else {
            execute(*array)
        }
    }

    protected override fun doInBackground(vararg strings: String?): String {
        var response = ""
        response = when (responseType) {
            Utils.CONNECT_TO_NETWORK -> connectToNetwork(*strings)
            Utils.CONNECT_TO_NETWORK_FOR_VALIDATE_USER -> validateUser(*strings)
            else -> getResponse(*strings)
        }
        return response
    }

    override fun onPostExecute(response: String) {
        super.onPostExecute(response)
        progressDialog.dismiss()
        rpcResponse.getRPCResponse(response, responseType)
    }

    private fun getResponse(vararg strings: String?): String {
        val query = strings[0]
        var response = ""
        try {
            NetworkManager.instance!!.sendToServer(query!!)
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
        }
        try {
            // Try now
            response = NetworkManager.instance!!.recvFromServer()
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
        }
        return response
    }

    private fun connectToNetwork(vararg strings: String?): String {
        val ip = strings[0]
        val port = strings[1]!!.toInt()
        val status = Boolean.valueOf(NetworkManager.instance!!.connectClient(ip, port))
        return status.toString()
    }

    private fun validateUser(vararg strings: String?): String {
        val tempdflUserId = strings[0]
        val tempdflPsswd = strings[1]
        val role = NetworkManager.instance!!.validateUser(tempdflUserId!!, tempdflPsswd!!)
        return role.toString()
    }


}