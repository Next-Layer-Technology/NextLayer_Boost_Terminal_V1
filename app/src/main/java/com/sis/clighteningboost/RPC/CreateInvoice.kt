package com.sis.clighteningboost.RPC

import com.sis.clighteningboost.RPC.NetworkManager
import kotlin.Throws
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode

class CreateInvoice {
    /*       resp,status,rpc-cmd,cli-node,[ {
                "payment_hash": "87d034804ee622234aee69914df7cac3f5d9eeaf256ea69f0fb45151363dacf4",
                        "expires_at": 1595368227,
                        "bolt11": "lnbc11200u1p0su29rpp5slgrfqzwuc3zxjhwdxg5ma72c06anm40y4h2d8c0k3g4zd3a4n6qdq92368gxqyjw5qcqp2sp5xj3jelx4alya3r4uahfy2839y4hussj30qzkl9lwrlhdg98tttsq9qy9qsq95dsrlq5399ten09juc5tz4cd8qk855fvv7wpsup93zkwtrnn9p4kf6q3hdhhqhfze6r37qdklsy7acd5pue0q883mtuyp9r4dhdvfsquudxqj",
                        "warning_deadends": "No channel with a peer that is not a dead end"
            }
 ]*/
    var payment_hash: String? = null
    var expires_at: Long = 0
    var bolt11: String? = null
    var warning_deadends: String? = null
}