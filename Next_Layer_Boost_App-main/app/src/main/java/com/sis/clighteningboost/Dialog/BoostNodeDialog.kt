package com.sis.clighteningboost.Dialog

import android.app.Activity
import android.app.Dialog
import com.sis.clighteningboost.Models.REST.MerchantNearbyClientsData
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sis.clighteningboost.R
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.sis.clighteningboost.Adapter.BoostNodeAdapter
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.DialogFragment
import com.sis.clighteningboost.Interface.OnCancelListener
import io.socket.client.Socket
import java.util.ArrayList

class BoostNodeDialog(
    var mActivity: Activity,
    var mFlag: Boolean,
    var mMerchantNearbyClientsDataArrayList: ArrayList<MerchantNearbyClientsData>,
    var mSocket: Socket
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.RoundedCornerDialog_MaterialComponents
        )
        val inflater = requireActivity().layoutInflater
        val view: View
        view = if (mFlag) inflater.inflate(
            R.layout.dialog_boost_scrollbar,
            null
        ) else inflater.inflate(R.layout.dialog_boost, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.layoutManager = gridLayoutManager
        val boostNodeAdapter: BoostNodeAdapter
        boostNodeAdapter =
            BoostNodeAdapter(mActivity, mMerchantNearbyClientsDataArrayList, mSocket,object :
                OnCancelListener{
                override fun onCancel() {
                    dismiss()
                }

            })
        /* if (mMerchantNearbyClientsDataArrayList!=null && !mMerchantNearbyClientsDataArrayList.isEmpty()){
        }else {
            boostNodeAdapter = new BoostNodeAdapter(mActivity,mSocket);
         }*/recyclerView.adapter = boostNodeAdapter
        val scrollbar = view.findViewById<AppCompatImageView>(R.id.ivScroll)
        scrollbar.setOnClickListener {
            val lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition()
            if (lastVisibleItemPosition < mMerchantNearbyClientsDataArrayList.size) {
                recyclerView.post { recyclerView.smoothScrollToPosition(lastVisibleItemPosition + 3) }
            }
        }
        dialogBuilder.setView(view)
        return dialogBuilder.create()
    }
}