package com.hoardersoft.colourselector;

import com.hoardersoft.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Test class for HSColourSelector.
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
public class HSColourSelectorTest {
    private HSColourSelectorWithLabel m_colourSelector = null;

    /**
     * Class constructor.
     */
    public HSColourSelectorTest() {
        HSBeanUtil.setLookAndFeel();

        JFrame frame = new JFrame("HSColourSelectorTest");

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("Colour Selected: " + m_colourSelector.getColour());
                System.exit(0);
            }
        });

        m_colourSelector = new HSColourSelectorWithLabel(new Color(255, 153, 204), "Select Colour");

        m_colourSelector.setLabelExpands(false);
        m_colourSelector.setLabelWidth(100);
        m_colourSelector.setNoColourEnabled(true);

        // m_colourSelector.setNoColourLabel("No Colour");
        // m_colourSelector.setMoreColoursLabel("Custom");
        m_colourSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Colour Changed: " + m_colourSelector.getColour());
            }
        });

        JPanel panel = new JPanel(new BorderLayout());

        panel.setPreferredSize(new Dimension(150, 50));
        panel.add(m_colourSelector, BorderLayout.CENTER);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new HSColourSelectorTest();
    }
}
