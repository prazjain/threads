package com.prash.threadlocal;

import java.text.SimpleDateFormat;

/**
 * Accompanying video:  https://www.youtube.com/watch?v=sjMe9aecW_A
 */
public class ThreadSafeFormatter {
    public static ThreadLocal<SimpleDateFormat> dateFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
        @Override
        public SimpleDateFormat get(){
            return super.get();
        }
    };

    public static ThreadLocal<SimpleDateFormat> dateFormatter2 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
}
