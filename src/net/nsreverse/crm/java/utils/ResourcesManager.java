package net.nsreverse.crm.java.utils;

public class ResourcesManager {
    public static String getLayoutPath(String filename) {
        return "/net/nsreverse/crm/res/layout/" + filename;
    }

    public static String getStylesheetPath(String filename) {
        return "/net/nsreverse/crm/res/styles/" + filename;
    }
}