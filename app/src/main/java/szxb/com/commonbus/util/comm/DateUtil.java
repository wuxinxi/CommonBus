package szxb.com.commonbus.util.comm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 作者: Tangren on 2017/7/8
 * 包名：com.szxb.onlinbus.util
 * 邮箱：996489865@qq.com
 * TODO:时间工具类
 */

public class DateUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh", "CN"));

    private static SimpleDateFormat format_1 = new SimpleDateFormat("yyyyMMdd", new Locale("zh", "CN"));

    private static SimpleDateFormat format_3 = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "CN"));

    //得到当前时间：yyyy-MM-dd HH:mm:ss
    public static String getCurrentTime() {
        return format.format(new Date());
    }

    //得到当前日期：yyyyMMdd
    public static String getCurrentDate() {
        return format.format(new Date());
    }

    //得到当前日期:yyyy-MM-dd
    public static String getBalckBegainDate() {
        return format_3.format(new Date());
    }

    //自定义格式获取当前日期
    public static String getCurrentDate(String format) {
        SimpleDateFormat ft = null;
        try {
            ft = new SimpleDateFormat(format, new Locale("zh", "CN"));
        } catch (Exception e) {
            return format_3.format(new Date());
        }
        return ft.format(new Date());
    }

    //获取当时Long型日期
    public static long currentLong() {
        return System.currentTimeMillis() / 1000;
    }

    //字符串转Date
    public static Date getLastDate(String lastDateString) {
        try {
            return format_3.parse(lastDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 保存Key的时间
     */
    public static String getSaveKeyDate() {
        return format_3.format(new Date());
    }

    /**
     * 判断是否可以更新
     *
     * @param lastDate 上次存储的日期
     * @return true可更新
     */
    public static boolean isUpdate(Date lastDate) {
        return differentDays(lastDate, Calendar.getInstance().getTime()) > 7;
    }


    /**
     * 文件是否过期
     *
     * @param lastDate 存储的时间
     * @return 是否删除
     */
    public static boolean isDelFile(Date lastDate) {
        return differentDays(lastDate, Calendar.getInstance().getTime()) > 90;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param lastDate    上次存储的日期
     * @param currentDate 现在的日期
     * @return 相差的天数
     */
    private static int differentDays(Date lastDate, Date currentDate) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(lastDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentDate);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            return day2 - day1;
        }
    }


    public static int getBetweenDay(Date date1, Date date2) {
        Calendar d1 = new GregorianCalendar();// 保存时候的日期
        d1.setTime(date1);
        Calendar d2 = new GregorianCalendar();// 现在的日期
        d2.setTime(date2);
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        System.out.println("days=" + days);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

}
