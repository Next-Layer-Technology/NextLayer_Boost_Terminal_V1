package com.sis.clighteningboost.Models.RPC;

public class RpcInfo {

    int id;
    String ip;
    String check_out_password;
    String check_out_user;
    String port;
    double registration_fees;

    public int getId() {
        return id;
    }


    public double getRegistration_fees() {
        return registration_fees;
    }

    public void setRegistration_fees(double registration_fees) {
        this.registration_fees = registration_fees;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCheck_out_password() {
        return check_out_password;
    }

    public void setCheck_out_password(String check_out_password) {
        this.check_out_password = check_out_password;
    }

    public String getCheck_out_user() {
        return check_out_user;
    }

    public void setCheck_out_user(String check_out_user) {
        this.check_out_user = check_out_user;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
