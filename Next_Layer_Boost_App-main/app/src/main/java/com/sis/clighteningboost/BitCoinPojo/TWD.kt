package com.sis.clighteningboost.BitCoinPojo

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.sis.clighteningboost.BitCoinPojo.USD
import com.sis.clighteningboost.BitCoinPojo.AUD
import com.sis.clighteningboost.BitCoinPojo.BRL
import com.sis.clighteningboost.BitCoinPojo.CAD
import com.sis.clighteningboost.BitCoinPojo.CHF
import com.sis.clighteningboost.BitCoinPojo.CLP
import com.sis.clighteningboost.BitCoinPojo.CNY
import com.sis.clighteningboost.BitCoinPojo.DKK
import com.sis.clighteningboost.BitCoinPojo.EUR
import com.sis.clighteningboost.BitCoinPojo.GBP
import com.sis.clighteningboost.BitCoinPojo.HKD
import com.sis.clighteningboost.BitCoinPojo.INR
import com.sis.clighteningboost.BitCoinPojo.ISK
import com.sis.clighteningboost.BitCoinPojo.JPY
import com.sis.clighteningboost.BitCoinPojo.KRW
import com.sis.clighteningboost.BitCoinPojo.NZD
import com.sis.clighteningboost.BitCoinPojo.PLN
import com.sis.clighteningboost.BitCoinPojo.RUB
import com.sis.clighteningboost.BitCoinPojo.SEK
import com.sis.clighteningboost.BitCoinPojo.SGD
import com.sis.clighteningboost.BitCoinPojo.THB
import com.sis.clighteningboost.BitCoinPojo.TRY
import com.sis.clighteningboost.BitCoinPojo.TWD

class TWD {
    @SerializedName("15m")
    @Expose
    private var _15m: Double? = null

    @SerializedName("last")
    @Expose
    var last: Double? = null

    @SerializedName("buy")
    @Expose
    var buy: Double? = null

    @SerializedName("sell")
    @Expose
    var sell: Double? = null

    @SerializedName("symbol")
    @Expose
    var symbol: String? = null

}