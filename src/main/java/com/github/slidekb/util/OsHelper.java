package com.github.slidekb.util;

import com.github.slidekb.api.Platform;

public class OsHelper {

    private static String OS = System.getProperty("os.name").toLowerCase();
    private static String ARCH = System.getProperty("os.arch").toLowerCase();

    public static Platform getOS() {
        if (isWindows()) {
            if (is64bit()) {
                return Platform.win64;
            } else {
                return Platform.win32;
            }
        } else if (isMac()) {
            if (is64bit()) {
                return Platform.osx64;
            } else {
                return Platform.osx32;
            }
        } else if (isUnix()) {
            if (is64bit()) {
                return Platform.nix64;
            } else {
                return Platform.nix32;
            }
        } else {
            return null;
        }
    }

    private static boolean is64bit() {
        return ARCH.endsWith("64");
    }

    private static boolean isWindows() {
        return (OS.contains("win"));
    }

    private static boolean isMac() {
        return (OS.contains("mac"));
    }

    private static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix") || OS.contains("sunos"));
    }
}
