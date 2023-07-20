package com.sis.clighteningboost.Models.RPC

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.sis.clighteningboost.Models.REST.ClientData
import com.sis.clighteningboost.Models.REST.TransactionInfo
import com.sis.clighteningboost.Models.REST.MerchantData
import com.sis.clighteningboost.Models.REST.FundingNode
import com.sis.clighteningboost.Models.REST.RoutingNode
import com.sis.clighteningboost.Models.REST.MerchantNearbyClientsData
import com.sis.clighteningboost.Models.TradeData
import com.sis.clighteningboost.Models.ARoutingAPIAuthData
import com.sis.clighteningboost.Models.NodesDataWithExecuteData
import com.sis.clighteningboost.Models.DecodeBolt112WithExecuteData

class Pay {
    /*  resp,status,rpc-cmd,cli-node,[ {
                "destination": "02dc8590dd675b5bf89c6bdf9eeed767290b3d6056465e5b013756f65616d3d372",
                        "payment_hash": "8c8fb25e7a1851944f2f10974549fa0845fbd480dde33569e4382a99a2ccd59d",
                        "created_at": 1596655318.504,
                        "parts": 1,
                        "msatoshi": 965000,
                        "amount_msat": "965000msat",
                        "msatoshi_sent": 965000,
                        "amount_sent_msat": "965000msat",
                        "payment_preimage": "881d6ee425fcb0b670191b140742364d35a4fc51a831197709756886aed8e7d7",
                        "status": "complete"
            }
 ]*/
    /*{   "destination": "02dc8590dd675b5bf89c6bdf9eeed767290b3d6056465e5b013756f65616d3d372",
       "payment_hash": "8c8fb25e7a1851944f2f10974549fa0845fbd480dde33569e4382a99a2ccd59d",
          "created_at": 1596664277.619,
            "parts": 1,
           "msatoshi": 965000,
           "amount_msat": "965000msat",
           "msatoshi_sent": 965000,
            "amount_sent_msat": "965000msat",   "
            payment_preimage": "881d6ee425fcb0b670191b140742364d35a4fc51a831197709756886aed8e7d7",
            "status": "complete"} */
    var destination: String? = null
    var payment_hash: String? = null
    var created_at = 0.0
    var parts = 0.0
    var msatoshi = 0.0
    var amount_msat: String? = null
    var msatoshi_sent = 0.0
    var amount_sent_msat: String? = null
    var payment_preimage: String? = null
    var status: String? = null
    var code = 0.0
    var message: String? = null
}