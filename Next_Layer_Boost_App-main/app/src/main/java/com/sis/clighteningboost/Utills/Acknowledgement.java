package com.sis.clighteningboost.Utills;

import android.util.Log;

import java.util.Arrays;

import io.socket.client.Ack;

public class Acknowledgement implements Ack {


    public Acknowledgement(Object... args){
        call(args);
    }
    @Override
    public void call(Object... args) {
    }



}
