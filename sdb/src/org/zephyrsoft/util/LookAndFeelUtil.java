package org.zephyrsoft.util;

import java.awt.*;

import javax.swing.*;

import org.zephyrsoft.sdb.*;

import com.jgoodies.looks.plastic.*;
import com.jgoodies.looks.plastic.theme.*;

public class LookAndFeelUtil {
	
	public static void setMyLookAndFeel() {
//		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
//		if (lookAndFeel != null) {
//			try {
//				UIManager.setLookAndFeel(lookAndFeel);
//			} catch (Exception e) {
//				System.out.println("Look&Feel konnte nicht gesetzt werden."); //$NON-NLS-1$
//			}
//		}
		
		PlasticLookAndFeel laf = new PlasticLookAndFeel();
		PlasticLookAndFeel.setCurrentTheme(new Silver());
		try {
			UIManager.setLookAndFeel(laf);
		} catch (Exception e) {
			System.out.println("Look&Feel konnte nicht gesetzt werden."); //$NON-NLS-1$
		}
	}
	
	public static Image getIcon() {
		return (new ImageIcon(GUI.class.getResource("/res/icon.gif"))).getImage(); //$NON-NLS-1$
	}
	
}
