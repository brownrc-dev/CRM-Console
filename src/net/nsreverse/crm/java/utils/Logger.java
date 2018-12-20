package net.nsreverse.crm.java.utils;

public class Logger {
    public static void d(String tag, String message) {
        System.out.println(String.format("%s/D > %s", tag, message));
    }

    public static void i(String tag, String message) {
        System.out.println(String.format("%s/I > %s", tag, message));
    }

    public static void e(String tag, String message) {
        System.out.println(String.format("%s/E > %s", tag, message));
    }
}
