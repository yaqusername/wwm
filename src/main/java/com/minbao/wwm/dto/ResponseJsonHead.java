package com.minbao.wwm.dto;

import java.io.Serializable;

public class ResponseJsonHead implements Serializable {

    private static final long serialVersionUID = 6182475286121010843L;

    private int st;

    private String msg;

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
