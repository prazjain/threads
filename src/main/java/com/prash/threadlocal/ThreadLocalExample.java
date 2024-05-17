package com.prash.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ThreadLocalExample {

    public static void main(String[] args) {

    }
    public String birthDate(int id) {
        Date birthDate = birthDateFromDb(id);
        final SimpleDateFormat df = ThreadSafeFormatter.dateFormatter.get();
        return df.format(birthDate);
    }
    public Date birthDateFromDb(int id) {
        return Calendar.getInstance().getTime();
    }
}
