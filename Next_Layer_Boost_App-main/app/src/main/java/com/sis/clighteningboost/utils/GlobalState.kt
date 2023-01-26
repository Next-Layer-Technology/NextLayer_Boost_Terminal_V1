package com.sis.clighteningboost.utils

import com.sis.clighteningboost.RPC.Tax
import com.sis.clighteningboost.Models.RPC.RpcInfo
import com.sis.clighteningboost.RPC.CreateInvoice
import com.sis.clighteningboost.RPC.Invoice
import com.sis.clighteningboost.Models.REST.RoutingNode
import com.sis.clighteningboost.Models.RPC.NodeLineInfo
import com.sis.clighteningboost.Models.REST.FundingNode
import com.sis.clighteningboost.Models.REST.MerchantData
import com.sis.clighteningboost.Models.REST.ClientData
import com.sis.clighteningboost.Models.RPC.InvoiceForPrint
import com.sis.clighteningboost.Models.RPC.DecodePayBolt11
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate
import java.util.ArrayList

class GlobalState {
    var tax: Tax? = null
    var rpcInfo: RpcInfo? = null
    var lattitude: String? = null
    var longitude: String? = null
    var createInvoice: CreateInvoice? = null
    var invoice: Invoice? = null
    var nodeArrayList: ArrayList<RoutingNode>? = null
    var clientNodeId: String? = null
    var nodeLineInfoArrayList: ArrayList<NodeLineInfo>? = null
    var fundingNode: FundingNode? = null
    var mainMerchantData: MerchantData? = null
    var mainClientData: ClientData? = null
    var allMerchantDataList: ArrayList<MerchantData>? = null
    var invoiceForPrint: InvoiceForPrint? = null
    var currentDecodePayBolt11: DecodePayBolt11? = null
    var currentAllRate: CurrentAllRate? = null
    fun addInNodeLineInfoArrayList(nodeLineInfo: NodeLineInfo) {
        if (nodeLineInfoArrayList != null) {
            nodeLineInfoArrayList!!.add(nodeLineInfo)
        } else {
            nodeLineInfoArrayList = ArrayList()
            nodeLineInfoArrayList!!.add(nodeLineInfo)
        }
    }

    fun removeInNodeLineInfoArrayList(nodeLineInfo: NodeLineInfo) {
        if (nodeLineInfoArrayList != null) {
            if (nodeLineInfoArrayList!!.size > 0) {
                nodeLineInfoArrayList!!.remove(nodeLineInfo)
            }
        }
    }

    companion object {
        @JvmStatic
        var globalState: GlobalState? = null
        @JvmStatic
        val instance: GlobalState?
            get() {
                if (globalState == null) {
                    globalState = GlobalState()
                }
                return globalState
            }
    }
}