package com.minbao.wwm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Description 时间工具类
 * @Author Wupeng
 * @Date
 * @Param
 * @return
 **/
public class DateUtils {
    /***
     * 格式化日期
     *
     * @param dt
     * @param format yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String formatDate(Date dt, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String sd = sdf.format(dt);
            return sd;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 时间字符串成转Date
     * @param strDate
     * @param dateFormat yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static Date stringToDate(String strDate,String dateFormat) {
        if(strDate!=null && !strDate.equals("")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                Date date =sdf.parse(strDate);
                return date;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else {
            return null;
        }
        return null;
    }

    /**
     * 时间增加减少
     * @param date
     * @param amount
     * @param type
     * @return
     */
    public static Date DatePush(Date date,int amount,int type){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (type == 1){
            calendar.add(calendar.YEAR, amount);//把日期往后增加一年.整数往后推,负数往前移动
        }else if (type == 2){
            calendar.add(Calendar.MONTH, amount);//把日期往后增加一个月.整数往后推,负数往前移动
        }else if (type == 3){
            calendar.add(calendar.DATE,amount);//把日期往后增加一天.整数往后推,负数往前移动
        }
        date=calendar.getTime();
        return date;
    }
    
    /**
             *        格式化时间
     * @param timeStr
     * @param format
     * @return
     */
	public static String formatTime(String timeStr,String format) {
		Date date = null;
		String formartTime = null;
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			date = formatter.parse(timeStr);
			formartTime = formatter.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formartTime;
	}
    
    /**
     * 时间戳 转 Date
     * @param longTime  时间戳
     * @param format
     * @return
     */
	public static String formatTime(Long longTime,String format) {
		String date = new SimpleDateFormat(format).format(new Date(longTime));
		return date;
	}
	
	
    
}



