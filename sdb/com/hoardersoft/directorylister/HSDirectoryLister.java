package com.hoardersoft.directorylister;

import com.hoardersoft.util.*;

import java.io.*;

/**
 * Class used to display a recursive directory listing.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
public class HSDirectoryLister {
    private static final String USAGE        = "Usage: java HSDirectoryLister <directory> [<title>]\n\nVersion: " + HSVersionUtil.VERSION;
    private static HSFileFilter m_fileFilter = new HSFileFilter();
    private static HSDirFilter m_dirFilter   = new HSDirFilter();

    /**
     * Main command line program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if ((args.length < 1) || args[0].equals("/?")) {
            System.out.println(USAGE);
            System.exit(0);
        }

        String directory = args[0];
        String title     = args[0];
        File root        = new File(directory);

        if (!root.exists() || !root.isDirectory()) {
            System.out.println("Error: <directory> must exist and must be a directory");
            System.out.println(USAGE);
            System.exit(0);
        }

        if (args.length > 1) {
            title = args[1];
        }

        System.out.println(title);
        System.out.println();
        listDirectory("", root);
    }

    private static void listDirectory(String prefix, File dir) {
        File[] files = dir.listFiles(m_fileFilter);
        File[] dirs  = dir.listFiles(m_dirFilter);

        // List directories first
        for (int i = 0; i < dirs.length; i++) {
            System.out.println(prefix + "+ " + dirs[i].getName());
            listDirectory("    " + prefix, dirs[i]);
        }

        // Then list files
        for (int i = 0; i < files.length; i++) {
            System.out.println(prefix + files[i].getName());
        }
    }
}

class HSFileFilter implements FileFilter {
    public boolean accept(File file) {
        return file.isFile();
    }
}

class HSDirFilter implements FileFilter {
    public boolean accept(File file) {
        return file.isDirectory();
    }
}
