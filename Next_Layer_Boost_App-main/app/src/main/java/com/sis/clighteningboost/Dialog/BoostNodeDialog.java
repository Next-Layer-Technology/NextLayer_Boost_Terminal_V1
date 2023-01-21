package com.sis.clighteningboost.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sis.clighteningboost.Adapter.BoostNodeAdapter;
import com.sis.clighteningboost.Models.REST.MerchantNearbyClientsData;
import com.sis.clighteningboost.R;

import java.util.ArrayList;

import io.socket.client.Socket;

public class BoostNodeDialog extends DialogFragment {
    boolean mFlag;
    Activity mActivity;
    ArrayList<MerchantNearbyClientsData> mMerchantNearbyClientsDataArrayList;
    Socket mSocket;

    public BoostNodeDialog(Activity activity, boolean flag, ArrayList<MerchantNearbyClientsData> merchantDataList,Socket socket){
        this.mFlag = flag;
        this.mActivity = activity;
        this.mMerchantNearbyClientsDataArrayList = merchantDataList;
        this.mSocket = socket;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        MaterialAlertDialogBuilder dialogBuilder =new MaterialAlertDialogBuilder(requireContext(), R.style.RoundedCornerDialog_MaterialComponents);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;
        if (mFlag)
            view = inflater.inflate(R.layout.dialog_boost_scrollbar, null);
        else
            view = inflater.inflate(R.layout.dialog_boost, null);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        BoostNodeAdapter boostNodeAdapter;
        boostNodeAdapter = new BoostNodeAdapter(mActivity,mMerchantNearbyClientsDataArrayList,mSocket, this::dismiss);
       /* if (mMerchantNearbyClientsDataArrayList!=null && !mMerchantNearbyClientsDataArrayList.isEmpty()){
        }else {
            boostNodeAdapter = new BoostNodeAdapter(mActivity,mSocket);
         }*/
        recyclerView.setAdapter(boostNodeAdapter);
        AppCompatImageView scrollbar = view.findViewById(R.id.ivScroll);
        scrollbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition < mMerchantNearbyClientsDataArrayList.size()) {
                     recyclerView.post(new Runnable() {
                         @Override
                         public void run() {
                             recyclerView.smoothScrollToPosition(lastVisibleItemPosition + 3);
                         }
                     });
                }
            }
        });

        dialogBuilder.setView(view);


        return dialogBuilder.create();

    }


}
