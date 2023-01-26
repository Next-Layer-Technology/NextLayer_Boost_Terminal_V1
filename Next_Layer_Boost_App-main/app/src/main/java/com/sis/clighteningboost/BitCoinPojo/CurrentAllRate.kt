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

class CurrentAllRate {
    @SerializedName("USD")
    @Expose
    var uSD: USD? = null

    @SerializedName("AUD")
    @Expose
    var aUD: AUD? = null

    @SerializedName("BRL")
    @Expose
    var bRL: BRL? = null

    @SerializedName("CAD")
    @Expose
    var cAD: CAD? = null

    @SerializedName("CHF")
    @Expose
    var cHF: CHF? = null

    @SerializedName("CLP")
    @Expose
    var cLP: CLP? = null

    @SerializedName("CNY")
    @Expose
    var cNY: CNY? = null

    @SerializedName("DKK")
    @Expose
    var dKK: DKK? = null

    @SerializedName("EUR")
    @Expose
    var eUR: EUR? = null

    @SerializedName("GBP")
    @Expose
    var gBP: GBP? = null

    @SerializedName("HKD")
    @Expose
    var hKD: HKD? = null

    @SerializedName("INR")
    @Expose
    var iNR: INR? = null

    @SerializedName("ISK")
    @Expose
    var iSK: ISK? = null

    @SerializedName("JPY")
    @Expose
    var jPY: JPY? = null

    @SerializedName("KRW")
    @Expose
    var kRW: KRW? = null

    @SerializedName("NZD")
    @Expose
    var nZD: NZD? = null

    @SerializedName("PLN")
    @Expose
    var pLN: PLN? = null

    @SerializedName("RUB")
    @Expose
    var rUB: RUB? = null

    @SerializedName("SEK")
    @Expose
    var sEK: SEK? = null

    @SerializedName("SGD")
    @Expose
    var sGD: SGD? = null

    @SerializedName("THB")
    @Expose
    var tHB: THB? = null

    @SerializedName("TRY")
    @Expose
    var tRY: TRY? = null

    @SerializedName("TWD")
    @Expose
    var tWD: TWD? = null
}