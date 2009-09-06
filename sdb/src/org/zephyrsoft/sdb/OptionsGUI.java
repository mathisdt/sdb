package org.zephyrsoft.sdb;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

import org.zephyrsoft.sdb.structure.*;
import org.zephyrsoft.util.*;

import com.hoardersoft.fontselector.*;

/**
 * GUI für die Optionen der SDB
 *
 * @author   Mathis Dirksen-Thedens
 */

public class OptionsGUI extends JFrame {

	private Options runtime = null;
	
	private GUI parent = null;
	JPanel contentPane;
	JButton okButton;
	JButton cancelButton;
	JLabel titelfont;
	JLabel textfont;
	JLabel translatefont;
	JLabel copyrightfont;
	JButton altertitelfont;
	JButton altertextfont;
	JButton altertranslatefont;
	JButton altercopyrightfont;
	JTextField spup;
	JTextField spleft;
	JTextField spdown;
	JTextField sptiteltext;
	JTextField sptextcopyright;
	JPanel fgdemo;
	JButton alterforeground;
	JPanel bgdemo;
	JButton alterbackground;
	JCheckBox beginn_statt_titel;
	JCheckBox zeige_titel;
	JCheckBox mehrere_folien;
	JTextField scroll_amount;
	JButton logoSelect;
	
	public OptionsGUI(GUI parent) {
		super(Messages.getString("OptionsGUI.0")); //$NON-NLS-1$
		this.parent = parent;
		this.runtime = parent.getOptions();
		
		// Aussehen
		LookAndFeelUtil.setMyLookAndFeel();
		setIconImage(LookAndFeelUtil.getIcon());
		// GUI definieren:
		contentPane = new JPanel();
		okButton = new JButton(Messages.getString("OptionsGUI.3")); //$NON-NLS-1$
		cancelButton = new JButton(Messages.getString("OptionsGUI.4")); //$NON-NLS-1$
		titelfont = new JLabel(Messages.getString("OptionsGUI.5")); //$NON-NLS-1$
		titelfont.setFont((Font)runtime.get("tifo")); //$NON-NLS-1$
		altertitelfont = new JButton(Messages.getString("OptionsGUI.7")); //$NON-NLS-1$
		textfont = new JLabel(Messages.getString("OptionsGUI.8")); //$NON-NLS-1$
		textfont.setFont((Font)runtime.get("tefo")); //$NON-NLS-1$
		altertextfont = new JButton(Messages.getString("OptionsGUI.7")); //$NON-NLS-1$
		translatefont = new JLabel(Messages.getString("OptionsGUI.57")); //$NON-NLS-1$
		translatefont.setFont((Font)runtime.get("trfo")); //$NON-NLS-1$
		altertranslatefont = new JButton(Messages.getString("OptionsGUI.7")); //$NON-NLS-1$
		copyrightfont = new JLabel(Messages.getString("OptionsGUI.11")); //$NON-NLS-1$
		copyrightfont.setFont((Font)runtime.get("cofo")); //$NON-NLS-1$
		altercopyrightfont = new JButton(Messages.getString("OptionsGUI.7")); //$NON-NLS-1$
		spup = new JTextField(runtime.get("spup").toString()); //$NON-NLS-1$
		spleft = new JTextField(runtime.get("sple").toString()); //$NON-NLS-1$
		spdown = new JTextField(runtime.get("spdo").toString()); //$NON-NLS-1$
		sptiteltext = new JTextField(runtime.get("sptt").toString()); //$NON-NLS-1$
		sptextcopyright = new JTextField(runtime.get("sptc").toString()); //$NON-NLS-1$
		fgdemo = new JPanel();
		fgdemo.add(new JLabel(" ")); //$NON-NLS-1$
		alterforeground = new JButton(Messages.getString("OptionsGUI.20")); //$NON-NLS-1$
		bgdemo = new JPanel();
		bgdemo.add(new JLabel(" ")); //$NON-NLS-1$
		alterbackground = new JButton(Messages.getString("OptionsGUI.22")); //$NON-NLS-1$
		beginn_statt_titel = new JCheckBox("", (runtime.get("bst")==null ? false : ((Boolean)runtime.get("bst")).booleanValue())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		zeige_titel = new JCheckBox("", (runtime.get("zt")==null ? true : ((Boolean)runtime.get("zt")).booleanValue())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		mehrere_folien = new JCheckBox("", (runtime.get("mf")==null ? false : ((Boolean)runtime.get("mf")).booleanValue())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		scroll_amount = new JTextField((runtime.get("scroll")==null ? "5" : runtime.get("scroll").toString())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		logoSelect = new JButton(Messages.getString("OptionsGUI.1")); //$NON-NLS-1$
		
		// GUI aufbauen:
		contentPane.setLayout(new GridLayout(18, 2));
		// vorne
		contentPane.add(titelfont);
		contentPane.add(altertitelfont);
		contentPane.add(textfont);
		contentPane.add(altertextfont);
		contentPane.add(translatefont);
		contentPane.add(altertranslatefont);
		contentPane.add(copyrightfont);
		contentPane.add(altercopyrightfont);
		contentPane.add(fgdemo);
		contentPane.add(alterforeground);
		contentPane.add(bgdemo);
		contentPane.add(alterbackground);
		contentPane.add(new JLabel(Messages.getString("OptionsGUI.32"))); //$NON-NLS-1$
		contentPane.add(spup);
		contentPane.add(new JLabel(Messages.getString("OptionsGUI.33"))); //$NON-NLS-1$
		contentPane.add(spleft);
		contentPane.add(new JLabel(Messages.getString("OptionsGUI.34"))); //$NON-NLS-1$
		contentPane.add(spdown);
		contentPane.add(new JLabel(Messages.getString("OptionsGUI.35"))); //$NON-NLS-1$
		contentPane.add(sptiteltext);
		contentPane.add(new JLabel(Messages.getString("OptionsGUI.36"))); //$NON-NLS-1$
		contentPane.add(sptextcopyright);
		contentPane.add(new JLabel(Messages.getString("OptionsGUI.37"))); //$NON-NLS-1$
		contentPane.add(beginn_statt_titel);
		contentPane.add(new JLabel(Messages.getString("OptionsGUI.38"))); //$NON-NLS-1$
		contentPane.add(zeige_titel);
		contentPane.add(new JLabel(Messages.getString("OptionsGUI.39") + GUI.SEPARATOR + "\"")); //$NON-NLS-1$ //$NON-NLS-2$
		contentPane.add(mehrere_folien);
		contentPane.add(new JLabel(Messages.getString("OptionsGUI.62"))); //$NON-NLS-1$
		contentPane.add(scroll_amount);
		contentPane.add(new JLabel(Messages.getString("OptionsGUI.2"))); //$NON-NLS-1$
		JPanel logoPanel = new JPanel();
		logoPanel.setLayout(new GridLayout(1, 2));
		logoPanel.add((parent.getLogoForOptions()!=null ? parent.getLogoForOptions() : (JComponent)new JLabel()));
		logoPanel.add(logoSelect);
		contentPane.add(logoPanel);
		
		contentPane.add(new JLabel(" ")); //$NON-NLS-1$
		contentPane.add(new JLabel(" ")); //$NON-NLS-1$
		contentPane.add(cancelButton);
		contentPane.add(okButton);
		
		this.setContentPane(contentPane);
		actualizeFonts();
		if (parent.getLogoForOptions()!=null) {
			parent.getLogoForOptions().scaleImage();
		}
		
		pack();
		setSize(new Dimension(650, 450));
		setLocation(new Point(300, 125));
		
		// EVENT HANDLER START =======================================
		alterforeground.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color newColor = JColorChooser.showDialog(
										 getThis(),
										 Messages.getString("OptionsGUI.43"), //$NON-NLS-1$
										 (Color)getRuntime().get("fgco")); //$NON-NLS-1$
					if (newColor!=null) {
					    getRuntime().put("fgco", newColor); //$NON-NLS-1$
					}
					actualizeFonts();
				}
			}
		);
		alterbackground.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color newColor = JColorChooser.showDialog(
										 getThis(),
										 Messages.getString("OptionsGUI.46"), //$NON-NLS-1$
										 (Color)getRuntime().get("bgco")); //$NON-NLS-1$
					if (newColor!=null) {
					    getRuntime().put("bgco", newColor); //$NON-NLS-1$
					}
					actualizeFonts();
				}
			}
		);
		altertextfont.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Font basefont = (Font)getRuntime().get("tefo"); //$NON-NLS-1$
					HSFontSelector fontsel = new HSFontSelector(getThis(), Messages.getString("OptionsGUI.50"), basefont); //$NON-NLS-1$
					Font font = fontsel.showDialog();
					if ( font == null ) {
						font = basefont;
					}
					getRuntime().put("tefo", font); //$NON-NLS-1$
					actualizeFonts();
				}
			}
		);
		altertranslatefont.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Font basefont = (Font)getRuntime().get("trfo"); //$NON-NLS-1$
						HSFontSelector fontsel = new HSFontSelector(getThis(), Messages.getString("OptionsGUI.58"), basefont); //$NON-NLS-1$
						Font font = fontsel.showDialog();
						if ( font == null ) {
							font = basefont;
						}
						getRuntime().put("trfo", font); //$NON-NLS-1$
						actualizeFonts();
					}
				}
			);
		altertitelfont.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Font basefont = (Font)getRuntime().get("tifo"); //$NON-NLS-1$
					HSFontSelector fontsel = new HSFontSelector(getThis(), Messages.getString("OptionsGUI.53"), basefont); //$NON-NLS-1$
					Font font = fontsel.showDialog();
					if ( font == null ) {
						font = basefont;
					}
					getRuntime().put("tifo", font); //$NON-NLS-1$
					actualizeFonts();
				}
			}
		);
		altercopyrightfont.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Font basefont = (Font)getRuntime().get("cofo"); //$NON-NLS-1$
					HSFontSelector fontsel = new HSFontSelector(getThis(), Messages.getString("OptionsGUI.56"), basefont); //$NON-NLS-1$
					Font font = fontsel.showDialog();
					if ( font == null ) {
						font = basefont;
					}
					getRuntime().put("cofo", font); //$NON-NLS-1$
					actualizeFonts();
				}
			}
		);
		logoSelect.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Datei aussuchen lassen
						String logoname = null;
						JFileChooser chooser = new JFileChooser();
						chooser.setMultiSelectionEnabled(false);
						chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						chooser.setFileFilter(new CustomFileFilter(Messages.getString("OptionsGUI.6"), new String[] {".jpg", ".jpeg", ".png", ".gif"})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
						chooser.showOpenDialog(OptionsGUI.this);
						logoname = (chooser.getSelectedFile()==null ? "" : chooser.getSelectedFile().getPath()); //$NON-NLS-1$
						
						// prüfen
						try {
							getGUI().loadLogo(logoname);
							if (getGUI().getLogoForOptions()!=null) {
								getGUI().getLogoForOptions().repaint();
							}
						} catch(Exception ex) {
							logoname = ""; //$NON-NLS-1$
						}
						// Einstellung speichern
						getRuntime().put("logo", logoname); //$NON-NLS-1$
					}
				}
			);
		okButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// die Abstände speichern
					try {
						getRuntime().put("spup", (new Integer(spup.getText())).intValue()<15 ? new Integer(15) : new Integer(spup.getText())); //$NON-NLS-1$
						getRuntime().put("sple", new Integer(spleft.getText())); //$NON-NLS-1$
						getRuntime().put("spdo", new Integer(spdown.getText())); //$NON-NLS-1$
						getRuntime().put("sptt", (new Integer(sptiteltext.getText())).intValue()<15 ? new Integer(15) : new Integer(sptiteltext.getText())); //$NON-NLS-1$
						getRuntime().put("sptc", (new Integer(sptextcopyright.getText())).intValue()<10 ? new Integer(10) : new Integer(sptextcopyright.getText())); //$NON-NLS-1$
						getRuntime().put("bst", new Boolean(beginn_statt_titel.isSelected())); //$NON-NLS-1$
						getRuntime().put("zt", new Boolean(zeige_titel.isSelected())); //$NON-NLS-1$
						getRuntime().put("mf", new Boolean(mehrere_folien.isSelected())); //$NON-NLS-1$
						getRuntime().put("scroll", (new Integer(scroll_amount.getText())).intValue()<1 ? new Integer(1) : new Integer(scroll_amount.getText())); //$NON-NLS-1$
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					/* schreiben */
					try {
						getGUI().saveOptions(runtime);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					dispose();
				}
			}
		);
		cancelButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			}
		);
		
		// EVENT HANDLER ENDE ========================================
		
		show();
	}
	
	public final void actualizeFonts() {
		// Fonts & Farben aus runtime als Darstellung nehmen
		titelfont.setFont((Font)runtime.get("tifo")); //$NON-NLS-1$
		textfont.setFont((Font)runtime.get("tefo")); //$NON-NLS-1$
		translatefont.setFont((Font)runtime.get("trfo")); //$NON-NLS-1$
		copyrightfont.setFont((Font)runtime.get("cofo")); //$NON-NLS-1$
		fgdemo.setBackground((Color)runtime.get("fgco")); //$NON-NLS-1$
		bgdemo.setBackground((Color)runtime.get("bgco")); //$NON-NLS-1$
	}
	
	public final GUI getGUI() {
		return parent;
	}
	
	public final OptionsGUI getThis() {
		return this;
	}
	
	public final Options getRuntime() {
		return runtime;
	}
}