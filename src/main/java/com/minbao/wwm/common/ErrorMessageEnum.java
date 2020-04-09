package com.minbao.wwm.common;

import java.util.HashMap;
import java.util.Map;

public enum ErrorMessageEnum {

    /**
     *
     */
    SERVICE_NOT_AVAILABLE(-99, "服务调用失败"),
    /**
     *
     */
    LOGIN_INCORRECT_PARAMETER(-98, "参数不正确"),
    /**
     *
     */
    INCORRECT_PARAMETER(-200, "参数不正确"),
    /**
     *
     */
    PARAMETER_TYPE_ERROR(-200, "pageSize不是数字类型"),
    /**
     *
     */
    DEL_FRIEND_FAILD_SERVICE(-3, "Serviceimpl错误!"),
    /**
     *
     */
    SERVICE_IMPL_ERROR(-3, "Serviceimpl错误!"),
    /**
     *
     */
    NO_PERMISSION(-3, "权限验证失败！"),
    /**
     *
     */
    FRIEND_IS_FIND(-3, "请勿重复添加！!"),
    /**
     *
     */
    FRIEND_IS_ONESELF(-3, "本系统不支持相同的身份添加好友!"),
    /**
     *
     */
    FRIEND_IS_NOT_FIND(-3, "好友Id与类型不一致！！"),

    /**
     *
     */
    FRIEND_APPLICATION_FIALD(-3, "好友申请失败"),

    /**
     *
     */
    QUERY_FRIEND_SUBMIT_INFO_ERROR(-3, "好友申请消息查看失败"),
    /**
     * 删除好友申请失败
     */
    FRIEND_APPLICATION_DELETE(-3, "删除好友申请失败"),
    /**
     *
     */
    DISPOSE_IS_NOT_FIND(-3, "查找失败!!"),
    /**
     *
     */
    DISPOSE_IS_ONESELF(-3, "好友已存在!"),
    /**
     *
     */
    DISPOSE_IS_ERROR(-3, "Serviceimpl错误!"),
    /**
     *
     */
    APPLYFOR_DELETE_NOT(-3, "Serviceimpl错误!"),

    /**
     *
     */
    USER_NAME_INEXISTENCE(-3, "用戶名不存在"),
    /**
     *
     */
    DELETE_SESSION_FAIL(-3, "刪除会话失敗"),

    ERROR_DATA_JSON(-3, "数据包解析错误"),

    ERROR_BEAN_TO_MAP(-3, "JAVA Bean 转 MAP 失败"), ERROR_MAP_TO_BEAN(-3, "JAVA MAP 转 Bean  失败"),

    /**
     *
     */
    PARAMETER_ERROR(-200, "参数异常"),
    //
    REGIST_NAME_PASSWORD_ERROR(10001001, "注册失败，请稍后再试."),
    //
    REGIST_NAME_IS_FUND(10001002, "用户名已存在，请更换用户名."),
    //
    REGIST_FAIL(10001003, "注册失败，请稍后再试."),
    //
    LOGIN_NAME_PASSWORD_ERROR(10002001, "用户名或密码错误."),
    //
    LOGIN__FAIL(10002002, "用户名或密码错误."),

    //
    RESET_PASSWORD_SECURITY_QUESTION(10003001, "密保问题答案错误."),
    //
    RESET_PASSWORD_FAIL(10003002, "重置密码失败."),
    //
    UPDATE_PASSWORD_VERIFICATION_FAIL(10004001, "原密码错误，请确认原密码."),
    //
    UPDATE_PASSWORD_FAIL(10004002, "密保问题答案错误."),
    //
    QUERY_USER_INFO_FAIL(10005001, "您的信息读取失败，请稍后再试."),
    //
    UPDATE_USER_INFO_FIAL(10006001, "您的信息修改失败，请稍后再试."),
    //
    UPDATE_EDUCATION_INFO_FIAL(10007001, "您的信息修改失败，请稍后再试."),
    //
    SECURITY_QUESTION_NOT_FOUND(10008001, "您尚未填写密保问题"),
    //
    QUERY_SECURITY_QUESTION_FIAL(10008002, "您的教育信息获取失败，请稍后再试"),
    //
    QUERY_EDUCATION_INFO_FIAL(10009001, "您的教育信息获取失败，请稍后再试"),
    //
    USERINFOSYNCHRONIZA_FIAL(100010001, "信息同步失败"),
    //
    QUERY_ADVERT_FAIL(20001001, "广告列表获取失败"),
    //
    TIME_COUNT_FAIL(20002001, "获取最近课堂失败"),
    //
    QUERY_TASK_FAIL(30001001, "我的作业获取失败"),
    //
    UPDATE_TASK_STATUS_FAIL(30002002, "我的作业获取失败"),
    //
    TASK_PICTURE_NOT_FOUND(30004001, "您未上传图片，请上传图片"),
    //
    QUERY_HISTROY_TEST_FAIL(40001001, "获取历史试卷失败"),
    //
    QUERY_CURRICULUM_FAIL(20003001, "获取课程失败");
    private int code;
    private String message;
    //private int cmd;
    private final static Map<String, ErrorMessageEnum> nameMap = new HashMap<String, ErrorMessageEnum>();
    private final static Map<Integer, ErrorMessageEnum> codeMap = new HashMap<Integer, ErrorMessageEnum>();

    static {
        for (ErrorMessageEnum errorMessage : ErrorMessageEnum.values()) {
            codeMap.put(errorMessage.getCode(), errorMessage);
            nameMap.put(errorMessage.getMessage(), errorMessage);
        }
    }

    ErrorMessageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//  public int getCmd() {
//    return cmd;
//  }
//
//  public void setCmd(int cmd) {
//    this.cmd = cmd;
//  }

    public static ErrorMessageEnum getFromName(String name) {
        return nameMap.get(name);
    }

    public static ErrorMessageEnum getFromCode(String code) {
        return codeMap.get(code);
    }
}
