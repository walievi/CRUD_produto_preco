package com.walievi;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Util {
    public static String dataToBr(Timestamp date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }
    

}
