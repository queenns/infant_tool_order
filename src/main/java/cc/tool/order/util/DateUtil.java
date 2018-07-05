package cc.tool.order.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lxj on 16-11-03
 */
public class DateUtil {

    public static final String DATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String formatSpecifyType(Date date, String type) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);

        return simpleDateFormat.format(date);

    }

    public static String createUnixDate(Long millis, int num) {

        Date date = findBeforeOrAfterMinute(new Date(millis), num);

        return Long.toString(date.getTime() / 1000);

    }

    /**
     * 给定一个时间Date　向前或向后n分钟得到新的日期
     *
     * @param date
     * @param num
     * @return
     */
    public static Date findBeforeOrAfterMinute(Date date, int num) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.MINUTE, num);

        return calendar.getTime();

    }


}
