package com.sis.clighteningboost.RPC

import com.sis.clighteningboost.RPC.NetworkManager
import kotlin.Throws
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode

class Invoice {
    var label: String? = null
    var bolt11: String? = null
    var payment_hash: String? = null
    var msatoshi = 0.0
    var amount_msat: String? = null
    var status: String? = null
    var pay_index = 0.0
    var msatoshi_received = 0.0
    var amount_received_msat: String? = null
    var paid_at: Long = 0
    var payment_preimage: String? = null
    var description: String? = null
    var expires_at: Long = 0
}