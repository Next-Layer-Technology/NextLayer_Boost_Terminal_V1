package com.sis.clighteningboost.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.sis.clighteningboost.Interface.RPCResponse;
import com.sis.clighteningboost.RPC.NetworkManager;
import com.sis.clighteningboost.Utills.StaticClass;
import com.sis.clighteningboost.Utills.Utils;

class DoInBackground extends AsyncTask<String,String,String> {

    ProgressDialog progressDialog;
    Activity parent;
    StaticClass st;
    String TAG = "tag";
    RPCResponse rpcResponse;
    int responseType;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    public DoInBackground(Activity parent,RPCResponse rpcResponse, int responseType,String message){

        this.parent = parent;
        st = new StaticClass(parent);
        progressDialog = new ProgressDialog(parent);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        this.rpcResponse = rpcResponse;
        this.responseType = responseType;
    }

    public void initExecute(String array[]){

        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, array);
        } else {
            execute(array);
        }

    }

    @Override
    protected String doInBackground(String... strings) {

        String response = "";

        switch (responseType){

            case Utils.CONNECT_TO_NETWORK: response = connectToNetwork(strings); break;
            case Utils.CONNECT_TO_NETWORK_FOR_VALIDATE_USER: response = validateUser(strings); break;
            default: response = getResponse(strings);

        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progressDialog.dismiss();
        rpcResponse.getRPCResponse(response,responseType);
    }


    private String getResponse(String ... strings){

        String query = strings[0];
        String response = "";

        try {
            NetworkManager.getInstance().sendToServer(query);
        } catch (Exception e) {
            Log.e(TAG,e.getLocalizedMessage());
        }
        try {
            // Try now
            response= NetworkManager.getInstance().recvFromServer();
        } catch (Exception e) {
            Log.e(TAG,e.getLocalizedMessage());
        }

        return response;
    }

    private String connectToNetwork(String ... strings){
        String ip = strings[0];
        int port = Integer.parseInt(strings[1]);
        Boolean status = Boolean.valueOf(NetworkManager.getInstance().connectClient(ip, port));
        return String.valueOf(status);
    }

    private String validateUser(String ... strings){

        String tempdflUserId = strings[0];
        String tempdflPsswd = strings[1];
        int role = NetworkManager.getInstance().validateUser(tempdflUserId, tempdflPsswd);
        return String.valueOf(role);
    }

}