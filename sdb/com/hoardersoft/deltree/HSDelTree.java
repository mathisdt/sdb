package com.hoardersoft.deltree;

import com.hoardersoft.util.*;

import java.io.*;

/**
 * Class to do a "deltree" as it used to known in DOS. Basically
 * just a recursive delete. Be careful with this one :)
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
public class HSDelTree {
    /**
     * Delete a file or directory recursively.
     *
     * @param file the file or directory to delete
     * @param verbose whether to produce verbose (debug) output
     * @return whether the file/directory was deleted
     */
    public static boolean delTree(File file, boolean verbose) {
        if (!file.exists()) {
            if (verbose) {
                System.out.println("File does not exist: " + file.getAbsolutePath());
            }

            return false;
        }

        if (file.isFile()) {
            boolean returnVal = file.delete();

            if (verbose) {
                System.out.println("Deleted file     : " + file.getAbsolutePath() + (returnVal ? "" : " (Unable to delete)"));
            }

            return returnVal;
        }
        else if (file.isDirectory()) {
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                delTree(files[i], verbose);
            }

            boolean returnVal = file.delete();

            if (verbose) {
                System.out.println("Deleted directory: " + file.getAbsolutePath() + (returnVal ? "" : " (Unable to delete)"));
            }

            return returnVal;
        }
        else {
            if (verbose) {
                System.out.println("Unable to access file: " + file.getAbsolutePath());
            }

            return false;
        }
    }

    /**
     * Main command line program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java HSDelTree file (or directory) [verbose]\n\nVersion: " + HSVersionUtil.VERSION);
            System.exit(0);
        }

        boolean verbose = ((args.length > 1) && args[1].equalsIgnoreCase("verbose"));

        delTree(new File(args[0]), verbose);
    }
}
