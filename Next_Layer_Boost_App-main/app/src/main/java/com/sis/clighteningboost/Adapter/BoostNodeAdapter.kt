package com.sis.clighteningboost.Adapter

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sis.clighteningboost.Activities.MerchantBoostTerminal
import com.sis.clighteningboost.Interface.OnCancelListener
import com.sis.clighteningboost.Models.REST.MerchantNearbyClientsData
import com.sis.clighteningboost.R
import com.sis.clighteningboost.utils.Acknowledgement
import de.hdodenhof.circleimageview.CircleImageView
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class BoostNodeAdapter(
    var mActivity: Activity,
    var mMerchantNearbyClientsDataArrayList: ArrayList<MerchantNearbyClientsData>,
    var mSocket: Socket,
    var cancelListener: OnCancelListener
) : RecyclerView.Adapter<BoostNodeAdapter.ViewHolder>() {
    var mClient_id = ""
    var mReceivingNodeId: String? = ""
    var mOnMsgReceived = false
    var mAlertDialog: Dialog? = null

    init {
        createAlertInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.item_boost_node, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            if (mMerchantNearbyClientsDataArrayList[position].client_image_url != null && !mMerchantNearbyClientsDataArrayList[position].client_image_url!!.isEmpty()) Glide.with(
                mActivity
            ).load(
                mMerchantNearbyClientsDataArrayList[position].client_image_url
            ).into(holder.mImageView)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        holder.itemView.setOnClickListener { view: View? ->
            if (mMerchantNearbyClientsDataArrayList[position].client_id != null && !mMerchantNearbyClientsDataArrayList[position].client_id!!.isEmpty()) {
                mClient_id = mMerchantNearbyClientsDataArrayList[position].client_id!!
            }
            val jsonObject = JSONObject()
            try {
                jsonObject.accumulate("to", mClient_id)
                jsonObject.accumulate("type", "fp_receiving_node_id_req")
                jsonObject.accumulate("payload", 1)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Log.d("Socket", "sending msg")
            Log.d("Socket", "isActive:${mSocket.isActive} connected:${mSocket.connected()}")

            mSocket.emit("msg", jsonObject, object : Acknowledgement() {
                override fun call(vararg args: Any) {
                    super.call(*args)
                    mActivity.runOnUiThread {
                        //                           if(args.length!=0){
                        Log.d("Socket", "Acknowledgement of zero element")
                        if (onStartReceive()) {
                            mActivity.startActivity(
                                Intent(
                                    mActivity,
                                    MerchantBoostTerminal::class.java
                                ).putExtra("node_id", mReceivingNodeId)
                            )
                        } else {
                            showAlert()
                        }
                        //                           }else {
//                               Toast.makeText(mActivity, "Message is not sent", Toast.LENGTH_SHORT).show();
//                           }
                    }
                }
            })

            android.os.Handler(Looper.getMainLooper()).postDelayed({
                Log.d("Socket", "isActive:${mSocket.isActive} connected:${mSocket.connected()}")
                Log.d("Socket", "finished")
            }, 5000)

        }
    }

    private fun showAlert() {
        mAlertDialog!!.show()
    }

    private fun createAlertInstance() {
        mAlertDialog = Dialog(mActivity)
        mAlertDialog!!.setContentView(R.layout.alert_dialog_layout)
        Objects.requireNonNull(mAlertDialog!!.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val alertTitle_tv = mAlertDialog!!.findViewById<TextView>(R.id.alertTitle)
        val alertMessage_tv = mAlertDialog!!.findViewById<TextView>(R.id.alertMessage)
        val yesbtn = mAlertDialog!!.findViewById<Button>(R.id.yesbtn)
        val nobtn = mAlertDialog!!.findViewById<Button>(R.id.nobtn)
        nobtn.visibility = View.GONE
        yesbtn.text = "Cancel Request"
        alertTitle_tv.text = "Flashpay Request Initiated"
        alertMessage_tv.text = "Please wait for Client info request auto-response"
        yesbtn.setOnClickListener { v: View? ->
            mAlertDialog!!.dismiss()
            cancelListener.onCancel()
        }
    }

    override fun getItemCount(): Int {
        return mMerchantNearbyClientsDataArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageView: CircleImageView
        var mIvBackground: AppCompatImageView

        init {
            mImageView = itemView.findViewById(R.id.ivImageView)
            mIvBackground = itemView.findViewById(R.id.ivBackground)
        }
    }

    fun onStartReceive(): Boolean {
        mOnMsgReceived = false
        mSocket.on("msg") { args: Array<Any> ->
            mActivity.runOnUiThread(
                {
                    val data: JSONObject = args.get(0) as JSONObject
                    Log.d("Socket", data.toString())
                    try {
                        mReceivingNodeId = data.getString("payload")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (mReceivingNodeId != null && !mReceivingNodeId!!.isEmpty()) {
                        mOnMsgReceived = true
                    }
                })
        }
        return mOnMsgReceived
    }
}