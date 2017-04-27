package com.github.slidekb.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CurrentWorkingDirectoryClassLoader {
    public static URLClassLoader getCurrentWorkingDirectoryClassLoader() {
        return getCurrentWorkingDirectoryClassLoader(null);
    }

    public static URLClassLoader getCurrentWorkingDirectoryClassLoader(String subDirectory) {
        File loc;

        if (subDirectory != null && !subDirectory.isEmpty()) {
            loc = new File(".", subDirectory);
        } else {
            loc = new File(".");
        }

        File[] flist = loc.listFiles(file -> file.getPath().toLowerCase().endsWith(".jar"));

        URL[] urls = new URL[flist.length];
        for (int i = 0; i < flist.length; i++) {
            try {
                urls[i] = flist[i].toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return new URLClassLoader(urls);
    }
}
