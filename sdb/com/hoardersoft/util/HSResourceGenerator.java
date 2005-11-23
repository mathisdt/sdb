package com.hoardersoft.util;

/**
 * Class to generate resource code and properties.
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
public class HSResourceGenerator {
    /**
     * Main command line program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java HSResourceGenerator <resource identifier> <type> <default value>");
            System.out.println("       (where type is one of string, icon, char, int, boolean)");
            System.exit(0);
        }

        String name         = args[0];
        E_ResourceType type = E_ResourceType.fromString(args[1].toUpperCase());
        String defaultValue = args[2];

        if (type == null) {
            System.out.println("Error: Type must be one of string, icon, char, int, boolean");
            System.exit(0);
        }

        String constant    = HSStringUtil.getDefaultConstant(name);
        String packageName = HSStringUtil.getDefaultPackage(name);

        System.out.println();

        if (type.equals(E_ResourceType.ICON)) {
            System.out.println("public static final ImageIcon " + constant + " = getIcon(\"" + packageName + "\");");
        }
        else if (type.equals(E_ResourceType.CHAR)) {
            System.out.println("public static final char " + constant + " = getCharacter(\"" + packageName + "\");");
        }
        else if (type.equals(E_ResourceType.INT)) {
            System.out.println("public static final int " + constant + " = getInteger(\"" + packageName + "\");");
        }
        else if (type.equals(E_ResourceType.BOOLEAN)) {
            System.out.println("public static final boolean " + constant + " = getBoolean(\"" + packageName + "\");");
        }
        else {
            // Default to string
            System.out.println("public static final String " + constant + " = getString(\"" + packageName + "\");");
        }

        System.out.println();
        System.out.println(packageName + "=" + defaultValue);
        System.out.println();
    }
}
