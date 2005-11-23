package com.hoardersoft.fontselector;

import com.hoardersoft.util.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Test class for HSFontSelector.
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
public class HSFontSelectorTest {
    /**
     * Main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HSBeanUtil.setLookAndFeel();

        final HSFontSelector selector = new HSFontSelector(null, "Please Choose Font");

        selector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Font Changed: " + selector.getCurrentFont());
            }
        });

        Font font = selector.showDialog();

        System.out.println("Font Selected: " + font);
        System.exit(0);
    }
}
