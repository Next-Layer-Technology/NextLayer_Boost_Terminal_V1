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

class DecodePayBolt11 {
    /*
    resp,status,rpc-cmd,cli-node,[ {
   "currency": "bc",
   "created_at": 1596653872,
   "expiry": 604800,
   "payee": "02dc8590dd675b5bf89c6bdf9eeed767290b3d6056465e5b013756f65616d3d372",
   "msatoshi": 965000,
   "amount_msat": "965000msat",
   "description": "firstrefundtest",
   "min_final_cltv_expiry": 10,
   "payment_secret": "65b2f135147ee24e3c28d03fb733c406b3ab84ef6fb554beae1dc75d78e506a1",
   "features": "028200",
   "payment_hash": "8c8fb25e7a1851944f2f10974549fa0845fbd480dde33569e4382a99a2ccd59d",
   "signature": "30440220049f8ddd183ae01fb1f242459b1a0504eea05e1bcb9eab180b481ab1d61943f20220229d14b37c52443063a98e69e58e55750a5d2d3130be106913ff75558b0a6818"
}
 ]*/
    var currency: String? = null
    var created_at: Long = 0
    var expiry: Long = 0
    var payee: String? = null
    var msatoshi = 0.0
    var amount_msat: String? = null
    var description: String? = null
    var min_final_cltv_expiry = 0.0
    var payment_secret: String? = null
    var features: String? = null
    var payment_hash: String? = null
    var signature: String? = null
}