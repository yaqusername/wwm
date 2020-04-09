package com.minbao.wwm.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestJson implements Serializable {

    private static final long serialVersionUID = 1L;

//    private RequestJsonHead head;

    private Map<String, Object> con;

//    public RequestJsonHead getHead() {
//        return head;
//    }

//    public void setHead(RequestJsonHead head) {
//        this.head = head;
//    }

    public Map<String, Object> getCon() {

        return con;

    }

    public void setCon(Map<String, Object> con) {
        this.con = con;
    }

    public String getConString(String key){
        if(con == null || !con.containsKey(key)){
            return null;
        }
        Object value = con.get(key);
        return (value == null)?null:String.valueOf(value);
    }
    public Integer getConInteger(String key){
        if(con == null || !con.containsKey(key)){
            return 0;
        }
        Object value = con.get(key);
        if(value == null) return 0;
        String str = String.valueOf(value);
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if(!pattern.matcher(str).matches()){
            return 0;
        }else{
            return Integer.valueOf(str);
        }
    }

    @Override
    public String toString() {
        return "RequestJson{" +
//                "head=" + head +
                ", con=" + con +
                '}';
    }
}
