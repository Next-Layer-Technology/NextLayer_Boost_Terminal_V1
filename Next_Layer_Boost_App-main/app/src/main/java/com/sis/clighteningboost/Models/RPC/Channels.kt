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

class Channels {
    //    @SerializedName("receivable_msatoshi")
    //    @Expose
    //     float receivable_msatoshi;
    //    @SerializedName("receivable_msat")
    //    @Expose
    //     String receivable_msat;
    //    @SerializedName("htlc_minimum_msat")
    //    @Expose
    //     float htlc_minimum_msat;
    //    @SerializedName("minimum_htlc_in_msat")
    //    @Expose
    //     String minimum_htlc_in_msat;
    //    @SerializedName("their_to_self_delay")
    //    @Expose
    //     float their_to_self_delay;
    //    @SerializedName("our_to_self_delay")
    //    @Expose
    //     float our_to_self_delay;
    //    @SerializedName("max_accepted_htlcs")
    //    @Expose
    //     float max_accepted_htlcs;
    //    @SerializedName("status")
    //    @Expose
    //    ArrayList<Status2> status = new ArrayList<Status2>();
    //    @SerializedName("in_payments_offered")
    //    @Expose
    //     float in_payments_offered;
    //    @SerializedName("in_msatoshi_offered")
    //    @Expose
    //     float in_msatoshi_offered;
    //    @SerializedName("in_offered_msat")
    //    @Expose
    //     String in_offered_msat;
    //    @SerializedName("in_payments_fulfilled")
    //    @Expose
    //     float in_payments_fulfilled;
    //    @SerializedName("in_msatoshi_fulfilled")
    //    @Expose
    //     float in_msatoshi_fulfilled;
    //    @SerializedName("in_fulfilled_msat")
    //    @Expose
    //     String in_fulfilled_msat;
    //    @SerializedName("out_payments_offered")
    //    @Expose
    //     float out_payments_offered;
    //    @SerializedName("out_msatoshi_offered")
    //    @Expose
    //     float out_msatoshi_offered;
    //    @SerializedName("out_offered_msat")
    //    @Expose
    //     String out_offered_msat;
    //    @SerializedName("out_payments_fulfilled")
    //    @Expose
    //     float out_payments_fulfilled;
    //    @SerializedName("out_msatoshi_fulfilled")
    //    @Expose
    //     float out_msatoshi_fulfilled;
    //    @SerializedName("out_fulfilled_msat")
    //    @Expose
    //     String out_fulfilled_msat;
    //    @SerializedName("htlcs")
    //    @Expose
    //    ArrayList<Htlcs> htlcs = new ArrayList<Htlcs>();
    //
    //    public String getState() {
    //        return state;
    //    }
    //
    //    public void setState(String state) {
    //        this.state = state;
    //    }
    //
    //    public String getScratch_txid() {
    //        return scratch_txid;
    //    }
    //
    //    public void setScratch_txid(String scratch_txid) {
    //        this.scratch_txid = scratch_txid;
    //    }
    //
    //    public String getShort_channel_id() {
    //        return short_channel_id;
    //    }
    //
    //    public void setShort_channel_id(String short_channel_id) {
    //        this.short_channel_id = short_channel_id;
    //    }
    //
    //    public float getDirection() {
    //        return direction;
    //    }
    //
    //    public void setDirection(float direction) {
    //        this.direction = direction;
    //    }
    //
    //    public String getChannel_id() {
    //        return channel_id;
    //    }
    //
    //    public void setChannel_id(String channel_id) {
    //        this.channel_id = channel_id;
    //    }
    //
    //    public String getFunding_txid() {
    //        return funding_txid;
    //    }
    //
    //    public void setFunding_txid(String funding_txid) {
    //        this.funding_txid = funding_txid;
    //    }
    //
    //    public String getClose_to_addr() {
    //        return close_to_addr;
    //    }
    //
    //    public void setClose_to_addr(String close_to_addr) {
    //        this.close_to_addr = close_to_addr;
    //    }
    //
    //    public String getClose_to() {
    //        return close_to;
    //    }
    //
    //    public void setClose_to(String close_to) {
    //        this.close_to = close_to;
    //    }
    //
    //    public boolean isPrivat() {
    //        return privat;
    //    }
    //
    //    public void setPrivat(boolean privat) {
    //        this.privat = privat;
    //    }
    //
    //    public ArrayList<Features> getFeatures() {
    //        return features;
    //    }
    //
    //    public void setFeatures(ArrayList<Features> features) {
    //        this.features = features;
    //    }
    //
    //    public Funding_allocation_msat getFunding_allocation_msatObject() {
    //        return Funding_allocation_msatObject;
    //    }
    //
    //    public void setFunding_allocation_msatObject(Funding_allocation_msat funding_allocation_msatObject) {
    //        Funding_allocation_msatObject = funding_allocation_msatObject;
    //    }
    //
    //    public Funding_msat getFunding_msatObject() {
    //        return Funding_msatObject;
    //    }
    //
    //    public void setFunding_msatObject(Funding_msat funding_msatObject) {
    //        Funding_msatObject = funding_msatObject;
    //    }
    //
    //    public float getMsatoshi_to_us() {
    //        return msatoshi_to_us;
    //    }
    //
    //    public void setMsatoshi_to_us(float msatoshi_to_us) {
    //        this.msatoshi_to_us = msatoshi_to_us;
    //    }
    //
    //    public String getTo_us_msat() {
    //        return to_us_msat;
    //    }
    //
    //    public void setTo_us_msat(String to_us_msat) {
    //        this.to_us_msat = to_us_msat;
    //    }
    //
    //    public float getMsatoshi_to_us_min() {
    //        return msatoshi_to_us_min;
    //    }
    //
    //    public void setMsatoshi_to_us_min(float msatoshi_to_us_min) {
    //        this.msatoshi_to_us_min = msatoshi_to_us_min;
    //    }
    //
    //    public String getMin_to_us_msat() {
    //        return min_to_us_msat;
    //    }
    //
    //    public void setMin_to_us_msat(String min_to_us_msat) {
    //        this.min_to_us_msat = min_to_us_msat;
    //    }
    //
    //    public float getMsatoshi_to_us_max() {
    //        return msatoshi_to_us_max;
    //    }
    //
    //    public void setMsatoshi_to_us_max(float msatoshi_to_us_max) {
    //        this.msatoshi_to_us_max = msatoshi_to_us_max;
    //    }
    //
    //    public String getMax_to_us_msat() {
    //        return max_to_us_msat;
    //    }
    //
    //    public void setMax_to_us_msat(String max_to_us_msat) {
    //        this.max_to_us_msat = max_to_us_msat;
    //    }
    //
    //    public float getMsatoshi_total() {
    //        return msatoshi_total;
    //    }
    //
    //    public void setMsatoshi_total(float msatoshi_total) {
    //        this.msatoshi_total = msatoshi_total;
    //    }
    //
    //    public String getTotal_msat() {
    //        return total_msat;
    //    }
    //
    //    public void setTotal_msat(String total_msat) {
    //        this.total_msat = total_msat;
    //    }
    //
    //    public float getDust_limit_satoshis() {
    //        return dust_limit_satoshis;
    //    }
    //
    //    public void setDust_limit_satoshis(float dust_limit_satoshis) {
    //        this.dust_limit_satoshis = dust_limit_satoshis;
    //    }
    //
    //    public String getDust_limit_msat() {
    //        return dust_limit_msat;
    //    }
    //
    //    public void setDust_limit_msat(String dust_limit_msat) {
    //        this.dust_limit_msat = dust_limit_msat;
    //    }
    //
    //    public float getMax_htlc_value_in_flight_msat() {
    //        return max_htlc_value_in_flight_msat;
    //    }
    //
    //    public void setMax_htlc_value_in_flight_msat(float max_htlc_value_in_flight_msat) {
    //        this.max_htlc_value_in_flight_msat = max_htlc_value_in_flight_msat;
    //    }
    //
    //    public String getMax_total_htlc_in_msat() {
    //        return max_total_htlc_in_msat;
    //    }
    //
    //    public void setMax_total_htlc_in_msat(String max_total_htlc_in_msat) {
    //        this.max_total_htlc_in_msat = max_total_htlc_in_msat;
    //    }
    //
    //    public float getTheir_channel_reserve_satoshis() {
    //        return their_channel_reserve_satoshis;
    //    }
    //
    //    public void setTheir_channel_reserve_satoshis(float their_channel_reserve_satoshis) {
    //        this.their_channel_reserve_satoshis = their_channel_reserve_satoshis;
    //    }
    //
    //    public String getTheir_reserve_msat() {
    //        return their_reserve_msat;
    //    }
    //
    //    public void setTheir_reserve_msat(String their_reserve_msat) {
    //        this.their_reserve_msat = their_reserve_msat;
    //    }
    //
    //    public float getOur_channel_reserve_satoshis() {
    //        return our_channel_reserve_satoshis;
    //    }
    //
    //    public void setOur_channel_reserve_satoshis(float our_channel_reserve_satoshis) {
    //        this.our_channel_reserve_satoshis = our_channel_reserve_satoshis;
    //    }
    //
    //    public String getOur_reserve_msat() {
    //        return our_reserve_msat;
    //    }
    //
    //    public void setOur_reserve_msat(String our_reserve_msat) {
    //        this.our_reserve_msat = our_reserve_msat;
    //    }
    //
    //    @SerializedName("state")
    //    @Expose
    //     String state;
    //    @SerializedName("scratch_txid")
    //    @Expose
    //     String scratch_txid;
    //    @SerializedName("short_channel_id")
    //    @Expose
    //     String short_channel_id;
    //    @SerializedName("direction")
    //    @Expose
    //     float direction;
    //    @SerializedName("channel_id")
    //    @Expose
    //     String channel_id;
    //    @SerializedName("funding_txid")
    //    @Expose
    //     String funding_txid;
    //    @SerializedName("close_to_addr")
    //    @Expose
    //     String close_to_addr;
    //    @SerializedName("close_to")
    //    @Expose
    //     String close_to;
    //    @SerializedName("")
    //    @Expose
    //     boolean privat;
    //    @SerializedName("features")
    //    @Expose
    //    ArrayList<Features> features = new ArrayList<Features>();
    //    @SerializedName("funding_allocation_msat")
    //    @Expose
    //    Funding_allocation_msat Funding_allocation_msatObject;
    //    @SerializedName("funding_msat")
    //    @Expose
    //    Funding_msat Funding_msatObject;
    //    @SerializedName("msatoshi_to_us")
    //    @Expose
    //     float msatoshi_to_us;
    //    @SerializedName("to_us_msat")
    //    @Expose
    //     String to_us_msat;
    //    @SerializedName("msatoshi_to_us_min")
    //    @Expose
    //     float msatoshi_to_us_min;
    //    @SerializedName("min_to_us_msat")
    //    @Expose
    //     String min_to_us_msat;
    //    @SerializedName("msatoshi_to_us_max")
    //    @Expose
    //     float msatoshi_to_us_max;
    //    @SerializedName("max_to_us_msat")
    //    @Expose
    //     String max_to_us_msat;
    //    @SerializedName("msatoshi_total")
    //    @Expose
    //     float msatoshi_total;
    //    @SerializedName("total_msat")
    //    @Expose
    //     String total_msat;
    //    @SerializedName("dust_limit_satoshis")
    //    @Expose
    //     float dust_limit_satoshis;
    //    @SerializedName("dust_limit_msat")
    //    @Expose
    //     String dust_limit_msat;
    //    @SerializedName("max_htlc_value_in_flight_msat")
    //    @Expose
    //     float max_htlc_value_in_flight_msat;
    //    @SerializedName("max_total_htlc_in_msat")
    //    @Expose
    //     String max_total_htlc_in_msat;
    //    @SerializedName("their_channel_reserve_satoshis")
    //    @Expose
    //     float their_channel_reserve_satoshis;
    //    @SerializedName("their_reserve_msat")
    //    @Expose
    //     String their_reserve_msat;
    //    @SerializedName("our_channel_reserve_satoshis")
    //    @Expose
    //     float our_channel_reserve_satoshis;
    //    @SerializedName("our_reserve_msat")
    //    @Expose
    //     String our_reserve_msat;
    @SerializedName("spendable_msatoshi")
    @Expose
     var spendable_msatoshi = 0.0

    @SerializedName("spendable_msat")
    @Expose
     var spendable_msat: String? = null

//
    //    public float getReceivable_msatoshi() {
    //        return receivable_msatoshi;
    //    }
    //
    //    public void setReceivable_msatoshi(float receivable_msatoshi) {
    //        this.receivable_msatoshi = receivable_msatoshi;
    //    }
    //
    //    public String getReceivable_msat() {
    //        return receivable_msat;
    //    }
    //
    //    public void setReceivable_msat(String receivable_msat) {
    //        this.receivable_msat = receivable_msat;
    //    }
    //
    //    public float getHtlc_minimum_msat() {
    //        return htlc_minimum_msat;
    //    }
    //
    //    public void setHtlc_minimum_msat(float htlc_minimum_msat) {
    //        this.htlc_minimum_msat = htlc_minimum_msat;
    //    }
    //
    //    public String getMinimum_htlc_in_msat() {
    //        return minimum_htlc_in_msat;
    //    }
    //
    //    public void setMinimum_htlc_in_msat(String minimum_htlc_in_msat) {
    //        this.minimum_htlc_in_msat = minimum_htlc_in_msat;
    //    }
    //
    //    public float getTheir_to_self_delay() {
    //        return their_to_self_delay;
    //    }
    //
    //    public void setTheir_to_self_delay(float their_to_self_delay) {
    //        this.their_to_self_delay = their_to_self_delay;
    //    }
    //
    //    public float getOur_to_self_delay() {
    //        return our_to_self_delay;
    //    }
    //
    //    public void setOur_to_self_delay(float our_to_self_delay) {
    //        this.our_to_self_delay = our_to_self_delay;
    //    }
    //
    //    public float getMax_accepted_htlcs() {
    //        return max_accepted_htlcs;
    //    }
    //
    //    public void setMax_accepted_htlcs(float max_accepted_htlcs) {
    //        this.max_accepted_htlcs = max_accepted_htlcs;
    //    }
    //
    //    public ArrayList<Status2> getStatus() {
    //        return status;
    //    }
    //
    //    public void setStatus(ArrayList<Status2> status) {
    //        this.status = status;
    //    }
    //
    //    public float getIn_payments_offered() {
    //        return in_payments_offered;
    //    }
    //
    //    public void setIn_payments_offered(float in_payments_offered) {
    //        this.in_payments_offered = in_payments_offered;
    //    }
    //
    //    public float getIn_msatoshi_offered() {
    //        return in_msatoshi_offered;
    //    }
    //
    //    public void setIn_msatoshi_offered(float in_msatoshi_offered) {
    //        this.in_msatoshi_offered = in_msatoshi_offered;
    //    }
    //
    //    public String getIn_offered_msat() {
    //        return in_offered_msat;
    //    }
    //
    //    public void setIn_offered_msat(String in_offered_msat) {
    //        this.in_offered_msat = in_offered_msat;
    //    }
    //
    //    public float getIn_payments_fulfilled() {
    //        return in_payments_fulfilled;
    //    }
    //
    //    public void setIn_payments_fulfilled(float in_payments_fulfilled) {
    //        this.in_payments_fulfilled = in_payments_fulfilled;
    //    }
    //
    //    public float getIn_msatoshi_fulfilled() {
    //        return in_msatoshi_fulfilled;
    //    }
    //
    //    public void setIn_msatoshi_fulfilled(float in_msatoshi_fulfilled) {
    //        this.in_msatoshi_fulfilled = in_msatoshi_fulfilled;
    //    }
    //
    //    public String getIn_fulfilled_msat() {
    //        return in_fulfilled_msat;
    //    }
    //
    //    public void setIn_fulfilled_msat(String in_fulfilled_msat) {
    //        this.in_fulfilled_msat = in_fulfilled_msat;
    //    }
    //
    //    public float getOut_payments_offered() {
    //        return out_payments_offered;
    //    }
    //
    //    public void setOut_payments_offered(float out_payments_offered) {
    //        this.out_payments_offered = out_payments_offered;
    //    }
    //
    //    public float getOut_msatoshi_offered() {
    //        return out_msatoshi_offered;
    //    }
    //
    //    public void setOut_msatoshi_offered(float out_msatoshi_offered) {
    //        this.out_msatoshi_offered = out_msatoshi_offered;
    //    }
    //
    //    public String getOut_offered_msat() {
    //        return out_offered_msat;
    //    }
    //
    //    public void setOut_offered_msat(String out_offered_msat) {
    //        this.out_offered_msat = out_offered_msat;
    //    }
    //
    //    public float getOut_payments_fulfilled() {
    //        return out_payments_fulfilled;
    //    }
    //
    //    public void setOut_payments_fulfilled(float out_payments_fulfilled) {
    //        this.out_payments_fulfilled = out_payments_fulfilled;
    //    }
    //
    //    public float getOut_msatoshi_fulfilled() {
    //        return out_msatoshi_fulfilled;
    //    }
    //
    //    public void setOut_msatoshi_fulfilled(float out_msatoshi_fulfilled) {
    //        this.out_msatoshi_fulfilled = out_msatoshi_fulfilled;
    //    }
    //
    //    public String getOut_fulfilled_msat() {
    //        return out_fulfilled_msat;
    //    }
    //
    //    public void setOut_fulfilled_msat(String out_fulfilled_msat) {
    //        this.out_fulfilled_msat = out_fulfilled_msat;
    //    }
    //
    //    public ArrayList<Htlcs> getHtlcs() {
    //        return htlcs;
    //    }
    //
    //    public void setHtlcs(ArrayList<Htlcs> htlcs) {
    //        this.htlcs = htlcs;
    //    }
}