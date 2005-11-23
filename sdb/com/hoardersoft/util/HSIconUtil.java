package com.hoardersoft.util;

import javax.swing.*;

import java.net.*;

/**
 * Class with utility methods for loading icons.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSIconUtil {
    private final static String DEFAULT_ICON_DIRECTORY = "com/hoardersoft/icons";

    /**
     * Loads an icon from the standard HoarderSoft icons directory.
     *
     * @param file the name of the icon to load
     * @return the icon, or null if not loaded
     */
    public static ImageIcon loadImage(String file) {
        return loadImage(file, null);
    }

    /**
     * Loads an icon from a given icons directory.
     *
     * @param file the name of the icon to load
     * @param dir the directory of the icon to load (null for the standard HoarderSoft icons directory)
     * @return the icon, or null if not loaded
     */
    public static ImageIcon loadImage(String file, String dir) {
        String actualFile;

        if (dir == null) {
            actualFile = DEFAULT_ICON_DIRECTORY + "/" + file;
        }
        else {
            if (dir.length() > 0) {
                actualFile = dir + "/" + file;
            }
            else {
                actualFile = file;
            }
        }

        URL url = HSIconUtil.class.getClassLoader().getResource(actualFile);

        if (url != null) {
            return new ImageIcon(url);
        }

        return null;
    }
}
