package com.minbao.wwm.common;

public class ErrorCMD {

    // 数据库status状态
    public static final int DB_SUCCESS = 0;// 成功
    public static final int DB_NOT_DATA = -1;// 没有数据
    public static final int DB_ERROR = -2;// 失败
    // 程序返回状态
    public static final int SUCCESS = 0;// 正确
    public static final int ERROR = -3;// 错误
    public static final int PARAMETER_ERROR=-5;
    public static final int NOT_PURVIEW = -4;// 没有权限
    public static final int NOT_DATA = -1000;// 没有数据
    public static final int TYPE_ERROR = -200;// 参数类型错误
    public static final int TYPE_ERROR_LOGIN = -201;// 参数类型错误
    public static final int TYPE_ERROR_PASSWORD = -201;// 旧密码错误
    public static final int ERROR_ORDER = -21;// 生成订单失败
    public static final int BUNDLING = -30;// 已绑定
    // result message
    public static final String SUCCESS_MESSAGE = "成功";
    public static final String ERROR_MESSAGE = "失败";
    public static final String NOT_DATA_MESSAGE = "没有数据";
    public static final String PARAMETER_MESSAGE="参数错误";
    public static final String ERROR_CMD = "命令错误！";
    public static final String ERROR_JSON_DATA = "数据包错误！";
    public static final String NOT_PURVIEW_MSG = "权限验证失败！";
    public static final String ERROR_PARAM = "参数异常！";
    public static final String ERROR_SQL = "数据库执行异常！";
    public static final String ERROR_PROCESS = "程序异常！";
    public static final String ERROR_NO_USER = "没有此用户";
    public static final String ERROR_NO_RECORD = "没有此记录";
    public static final String ERROR_LOGIN = "用户名或密码错误";
    public static final String ERROR_REGISTER = "用户名已存在，请更换用户名";
}
