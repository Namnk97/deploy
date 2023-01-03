package com.nextsol.khangbb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat sdtf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    public static SimpleDateFormat dateUpFile = new SimpleDateFormat("ddMMyyyyhhmmss");

    public static Date today() {


        return new Date();
    }

    public static String todayStr() {
        return sdf.format(today());
    }

    public static String dateUpFile() {
        return dateUpFile.format(today());
    }

    public static String formattedDate(Date date) {
        return date != null ? sdf.format(date) : todayStr();
    }
}
