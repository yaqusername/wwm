package com.minbao.wwm.dto;

import com.minbao.wwm.common.ErrorCMD;
import com.minbao.wwm.common.ErrorMessageEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResponseJson implements Serializable {

    private static final long serialVersionUID = 8264150831402609392L;

//    private ResponseJsonHead head;

    private Map<String, Object> body;

//    public ResponseJsonHead getHead() {
//        return head;
//    }

//    public void setHead(ResponseJsonHead head) {
//        this.head = head;
//    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public ResponseJson() {
        super();
    }

    public ResponseJson(Map<String, Object> body) {
        super();
//        this.head = head;
        this.body = body;
    }

    public ResponseJson(int status, String msg) {
        ResponseJsonHead head = new ResponseJsonHead();
        head.setSt(status);
        head.setMsg(msg);
//        this.head = head;
        this.body = new HashMap<String, Object>();
    }

    public ResponseJson(ErrorMessageEnum errorMessage) {
        ResponseJsonHead head = new ResponseJsonHead();
        head.setSt(errorMessage.getCode());
        head.setMsg(errorMessage.getMessage());
//        this.head = head;
        this.body = new HashMap<String, Object>();
    }

    public ResponseJson(int status, String msg, Map<String, Object> body) {
        ResponseJsonHead head = new ResponseJsonHead();
        head.setSt(status);
        head.setMsg(msg);
//        this.head = head;
        if (body != null && body.size() > 0) {
            this.body = body;
        } else {
            this.body = new HashMap<String, Object>();
        }
    }

    public ResponseJson getResponseJson(int status, String msg, Map<String, Object> body) {
        // 返回包头
        ResponseJsonHead responseJsonHead = new ResponseJsonHead();
        responseJsonHead.setSt(status);
        responseJsonHead.setMsg(msg);
//        this.setHead(responseJsonHead);// 设置包头
        // 返回体
        if (status == ErrorCMD.SUCCESS) {
            this.setBody(body);
        }
        return this;
    }
}
