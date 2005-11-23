package org.zephyrsoft.sdb;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

import org.zephyrsoft.sdb.structure.*;

/**
 * Beamer-Projektionsansicht für die Lieder-Datenbank
 * @author Mathis Dirksen-Thedens
 */

public class BeamerView extends JFrame {

	private boolean debug = false;
	
	JPanel contentPane = null;
	BorderLayout borderLayout = null;
	JScrollPane scrollPane = null;
	JPanel back = null;
	
	private JLabel titel;
	private JTextPane text;
//	private JTextArea copyright;
	
	private Song actualSong = null;
	
	private int actualFoil = 0;
	private Font titelfont = null;
	private Font textfont = null;
	private Font translatefont = null;
	private Font copyrightfont = null;
	
	private BeamerGUI parent;
	
	private Dimension dim = null;
	
	public BeamerView(Song song, Font titelfont, Font textfont, Font translatefont, Font copyrightfont, boolean printAccords, BeamerGUI parent, GraphicsConfiguration gc, int foil) {
		//super(parent, gc);
		super(Messages.getString("BeamerView.0"), gc); //$NON-NLS-1$
		setIconImage((new ImageIcon("res/icon.gif")).getImage()); //$NON-NLS-1$
		this.titelfont = titelfont;
		this.textfont = textfont;
		this.translatefont = translatefont;
		this.copyrightfont = copyrightfont;
		this.parent = parent;
		
		// GUI-Elemente definieren
		contentPane = new JPanel();
		borderLayout = new BorderLayout();
		
		this.setLocale(java.util.Locale.getDefault());
		
		// nichts standardmäßig tun, nur exit() darf das Fenster schließen!
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		GraphicsDevice device = devices[devices.length - 1];
		dim = new Dimension(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
		
		showSong(song, titelfont, textfont, translatefont, copyrightfont, printAccords, foil);
		
		// EVENT HANDLER START =======================================
		this.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					exit();
				}
			}
		);
		
		// EVENT HANDLER ENDE ========================================
		
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(
						  new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor =
			Toolkit.getDefaultToolkit().createCustomCursor
			(image, new Point(0, 0), "invisiblecursor"); //$NON-NLS-1$
		this.setCursor(transparentCursor);
		back.setCursor(transparentCursor);
		scrollPane.setCursor(transparentCursor);
		titel.setCursor(transparentCursor);
		text.setCursor(transparentCursor);
//		copyright.setCursor(transparentCursor);
		setResizable(false);
		setUndecorated(true);
		this.getRootPane().setWindowDecorationStyle(NONE);
		pack();
		show();
		
		// global DoubleBuffering einschalten:
		RepaintManager.currentManager(back).setDoubleBufferingEnabled(true);
		
		// groß auf letztem verfügbaren Bildschirm machen (wird wohl der Beamer sein):
		try {
			this.setSize(dim);
			scrollPane.getViewport().setSize(dim);
			
			// Fullscreen!
			//System.out.println("FULLSCREENSUPPORTED=" + device.isFullScreenSupported());
			//device.setFullScreenWindow(this);
			
			this.validate();
		} catch (Exception ex) {
			//device.setFullScreenWindow(null);
			System.out.println("ERROR WHILE SETTING WINDOW TO FULLSCREEN: " + ex.getMessage()); //$NON-NLS-1$
		}
		
	}
	
//	public void showSong(Song newsong, Font titelfont, Font textfont, Font copyrightfont, boolean printAccords) {
//		showSong(newsong, titelfont, textfont, copyrightfont, printAccords, 0);
//	}

	public void showSong(Song newsong, Font titelfont, Font textfont, Font translatefont, Font copyrightfont, boolean printAccords, int foil) {
	    this.titelfont = titelfont;
		this.textfont = textfont;
		this.translatefont = translatefont;
		this.copyrightfont = copyrightfont;
		actualSong = newsong;
		actualFoil = foil;
		
		//blackScreen(true);
		
		back = new JPanel();
		back.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
		back.setLayout(new BorderLayout());
		
		// GUI formen
		scrollPane = new JScrollPane(back);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setDoubleBuffered(true);
		
		this.setContentPane(scrollPane.getViewport());
		
		try {
			this.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
			this.getRootPane().setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
			this.getContentPane().setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
		} catch (Exception ex) {}
		back.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
		back.repaint();
		
		titel = new JLabel(parent.getNormalTitleByID(newsong.getID()));
		text = new JTextPane() {
		    public Dimension getPreferredSize(){
		        Dimension size = super.getPreferredSize();
		        // "sple"="space on left side"; 15="space on right side"
		        size.width = getSize().width - ((Integer)parent.getOptions().get("sple")).intValue() - 15; //$NON-NLS-1$
		        return size;
		    }
		};
		if (((Boolean)parent.parent.getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
			if ( printAccords ) {
				text.setText(newsong.getTextAndAccordsInFont_Foil(textfont, false, foil));
			} else {
				text.setText(newsong.getOnlyText_Foil(foil));
			}
		} else {
			if ( printAccords ) {
				text.setText(newsong.getTextAndAccordsInFont(textfont, false));
			} else {
				text.setText(newsong.getOnlyText());
			}
		}
		
		// Text nach reg. Ausdruck "[.+]" durchsuchen
		Vector vonalle = new Vector();
		Vector bisalle = new Vector();
		String text1 = text.getText();
		int von = text1.indexOf("["); //$NON-NLS-1$
		while (von >= 0) {
		    int vorher = 0; // zählt Leerzeichen vorher
		    while (text1.substring(von-vorher-1,von-vorher).equals(" ")) { //$NON-NLS-1$
			    vorher++;
			}
		    text1 = text1.substring(0, von) + text1.substring(von+1);
		    vonalle.addElement(new Integer(von-vorher));
		    int bis = text1.indexOf("]", von); //$NON-NLS-1$
		    int nachher = 0; // zählt Leerzeichen nachher
			while (text1.substring(bis+nachher+1,bis+nachher+2).equals(" ")) { //$NON-NLS-1$
			    nachher++;
			}
			text1 = text1.substring(0, bis) + text1.substring(bis+1);
		    bisalle.addElement(new Integer(bis+nachher));
		    von = text1.indexOf("["); //$NON-NLS-1$
		}
		text.setText(text1 + "\n" + newsong.getCopyright());
		
		StyledDocument doc = text.getStyledDocument();
		Style defaultstyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		
		Style titlestyle = doc.addStyle("title", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(titlestyle, titelfont.isItalic());
		StyleConstants.setBold(titlestyle, titelfont.isBold());
		StyleConstants.setFontFamily(titlestyle, titelfont.getFamily());
		StyleConstants.setFontSize(titlestyle, titelfont.getSize());
		
		Style textstyle = doc.addStyle("text", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(textstyle, textfont.isItalic());
		StyleConstants.setBold(textstyle, textfont.isBold());
		StyleConstants.setFontFamily(textstyle, textfont.getFamily());
		StyleConstants.setFontSize(textstyle, textfont.getSize());
		
		Style translatestyle = doc.addStyle("translate", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(translatestyle, translatefont.isItalic());
		StyleConstants.setBold(translatestyle, translatefont.isBold());
		StyleConstants.setFontFamily(translatestyle, translatefont.getFamily());
		StyleConstants.setFontSize(translatestyle, translatefont.getSize());
		
		Style sptc = doc.addStyle("sptc", defaultstyle); //$NON-NLS-1$
		StyleConstants.setFontSize(sptc, ((Integer)parent.getOptions().get("sptc")).intValue());
		
		Style copyrightstyle = doc.addStyle("copyright", defaultstyle); //$NON-NLS-1$
		StyleConstants.setItalic(copyrightstyle, copyrightfont.isItalic());
		StyleConstants.setBold(copyrightstyle, copyrightfont.isBold());
		StyleConstants.setFontFamily(copyrightstyle, copyrightfont.getFamily());
		StyleConstants.setFontSize(copyrightstyle, copyrightfont.getSize());
		
		// Text-Style zuweisen
		doc.setCharacterAttributes(0, text1.length(), doc.getStyle("text"), false); //$NON-NLS-1$
		
		// Translate-Style zuweisen
		for (int i = 0; i < vonalle.size(); i++) {
		    int von1 = ((Integer)vonalle.elementAt(i)).intValue();
		    int bis1 = ((Integer)bisalle.elementAt(i)).intValue();
		    try {
			    if (text.getText(bis1,1).equals("\n")) { //$NON-NLS-1$
			        bis1++;
			    }
		    } catch(Exception ex) {
		        ex.printStackTrace();
		    }
		    doc.setCharacterAttributes(von1, bis1-von1, doc.getStyle("translate"), false); //$NON-NLS-1$
		}
		// Abstand zwischen Text und Copyright zuweisen
		doc.setCharacterAttributes(text1.length(), text1.length() + 1, doc.getStyle("sptc"), false); //$NON-NLS-1$
		
		// Copyright-Style zuweisen
		doc.setCharacterAttributes(text1.length() + 1, text1.length() + 1 + newsong.getCopyright().length(), doc.getStyle("copyright"), false); //$NON-NLS-1$

		
		titel.setFont((Font)parent.getOptions().get("tifo")); //$NON-NLS-1$
		text.setFont((Font)parent.getOptions().get("tefo")); //$NON-NLS-1$
		text.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
		titel.setForeground((Color)parent.getOptions().get("fgco")); //$NON-NLS-1$
		text.setForeground((Color)parent.getOptions().get("fgco")); //$NON-NLS-1$
		
		titel.setRequestFocusEnabled(false);
		text.setRequestFocusEnabled(false);
		
		/*
			Trick, damit die ÄÖÜ-Punkte in den ersten Zeilen angezeigt werden:
			Rand oben auf 15 Pixel setzen, dann schneidet Linux die Umlaut-
			Punkte nicht mehr ab! Natürlich muss dann die Position und Größe
			entsprechend korrigiert werden...
		*/
		text.setBorder(BorderFactory.createEmptyBorder(((Integer)parent.getOptions().get(((parent.getOptions().get("zt")==null ? true : (((Boolean)parent.getOptions().get("zt")).booleanValue())) ? "spup" : "sptt"))).intValue(), ((Integer)parent.getOptions().get("sple")).intValue(),0,15));
		text.setEditable(false);
//		back.setIgnoreRepaint(true);
		back.add(text, BorderLayout.CENTER);
		back.setBorder(BorderFactory.createEmptyBorder(0, 0, ((Integer)parent.getOptions().get("spdo")).intValue(), 0));
		if (parent.getOptions().get("zt")==null ? true : (((Boolean)parent.getOptions().get("zt")).booleanValue())) { //$NON-NLS-1$ //$NON-NLS-2$
		    titel.setBorder(BorderFactory.createEmptyBorder(((Integer)parent.getOptions().get("spup")).intValue(), ((Integer)parent.getOptions().get("sple")).intValue(),15,15));
			back.add(titel, BorderLayout.NORTH);
		}

		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        scroll(back, TOP);
//		        back.setIgnoreRepaint(false);
		    }
		});
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(
						  new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor =
			Toolkit.getDefaultToolkit().createCustomCursor
			(image, new Point(0, 0), "invisiblecursor"); //$NON-NLS-1$
		this.setCursor(transparentCursor);
		back.setCursor(transparentCursor);
		scrollPane.setCursor(transparentCursor);
		titel.setCursor(transparentCursor);
		text.setCursor(transparentCursor);

		//blackScreen(false);
	}
	
	public int nextFoil() {
		return nextFoil(false);
	}
	public int nextFoil(boolean printAccords) {
		int newfoil = actualFoil + 1;
		if (newfoil >= actualSong.getFoilCount()) {
			newfoil = actualSong.getFoilCount()-1;
		}
		if (actualFoil != newfoil) {
			showFoil(newfoil, printAccords);
		}
		return newfoil;
	}
	
	public int previousFoil() {
		return previousFoil(false);
	}
	public int previousFoil(boolean printAccords) {
		int newfoil = actualFoil - 1;
		if (newfoil < 0) {
			newfoil = 0;
		}
		if (actualFoil != newfoil) {
			showFoil(newfoil, printAccords);
		}
		return newfoil;
	}
	
	public boolean goToFoil(int foil) {
		return goToFoil(foil, false);
	}
	public boolean goToFoil(int foil, boolean printAccords) {
		if (foil < 0) {
			foil = 0;
		}
		if (actualFoil != foil) {
			showFoil(foil, printAccords);
			return true;
		}
		return false;
	}
	
	private void showFoil(int newfoil, boolean printAccords) {
	    showSong(actualSong, titelfont, textfont, translatefont, copyrightfont, printAccords, newfoil);
	}
	
//	private void showFoil(int newfoil, boolean printAccords) {
//		
//	    actualFoil = newfoil;
//	    
//		back.removeAll();
//		try {
//			this.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
//			this.getRootPane().setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
//			this.getContentPane().setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
//		} catch (Exception ex) {}
//		back.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
//		back.repaint();
//		
//		
//		// ((Boolean)parent.parent.getOptions().get("bst")).booleanValue()
//		titel = new JLabel(parent.getNormalTitleByID(actualSong.getID()));
//		text = new JTextPane();
//		if (((Boolean)parent.parent.getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
//			if ( printAccords ) {
//				text.setText(actualSong.getTextAndAccordsInFont_Foil(textfont, false, newfoil));
//			} else {
//				text.setText(actualSong.getOnlyText_Foil(newfoil));
//			}
//		} else {
//			if ( printAccords ) {
//				text.setText(actualSong.getTextAndAccordsInFont(textfont, false));
//			} else {
//				text.setText(actualSong.getOnlyText());
//			}
//		}
//		
//		// Text nach reg. Ausdruck "[.+]" durchsuchen
//		Vector vonalle = new Vector();
//		Vector bisalle = new Vector();
//		String text1 = text.getText();
//		int von = text1.indexOf("["); //$NON-NLS-1$
//		while (von >= 0) {
//		    text1 = text1.substring(0, von) + text1.substring(von+1);
//		    vonalle.addElement(new Integer(von));
//		    int bis = text1.indexOf("]", von); //$NON-NLS-1$
//		    text1 = text1.substring(0, bis) + text1.substring(bis+1);
//		    bisalle.addElement(new Integer(bis));
//		    von = text1.indexOf("["); //$NON-NLS-1$
//		}
//		text.setText(text1);
//		
//		StyledDocument doc = text.getStyledDocument();
//		Style defaultstyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
//		
//		Style titlestyle = doc.addStyle("title", defaultstyle); //$NON-NLS-1$
//		StyleConstants.setItalic(titlestyle, titelfont.isItalic());
//		StyleConstants.setBold(titlestyle, titelfont.isBold());
//		StyleConstants.setFontFamily(titlestyle, titelfont.getFamily());
//		StyleConstants.setFontSize(titlestyle, titelfont.getSize());
//		
//		Style textstyle = doc.addStyle("text", defaultstyle); //$NON-NLS-1$
//		StyleConstants.setItalic(textstyle, textfont.isItalic());
//		StyleConstants.setBold(textstyle, textfont.isBold());
//		StyleConstants.setFontFamily(textstyle, textfont.getFamily());
//		StyleConstants.setFontSize(textstyle, textfont.getSize());
//		
//		Style translatestyle = doc.addStyle("translate", defaultstyle); //$NON-NLS-1$
//		StyleConstants.setItalic(translatestyle, translatefont.isItalic());
//		StyleConstants.setBold(translatestyle, translatefont.isBold());
//		StyleConstants.setFontFamily(translatestyle, translatefont.getFamily());
//		StyleConstants.setFontSize(translatestyle, translatefont.getSize());
//		
//		Style copyrightstyle = doc.addStyle("copyright", defaultstyle); //$NON-NLS-1$
//		StyleConstants.setItalic(copyrightstyle, copyrightfont.isItalic());
//		StyleConstants.setBold(copyrightstyle, copyrightfont.isBold());
//		StyleConstants.setFontFamily(copyrightstyle, copyrightfont.getFamily());
//		StyleConstants.setFontSize(copyrightstyle, copyrightfont.getSize());
//		
//		// Translate-Style zuweisen (die anderen werden noch nicht benutzt)
//		for (int i = 0; i < vonalle.size(); i++) {
//		    int von1 = ((Integer)vonalle.elementAt(i)).intValue();
//		    int bis1 = ((Integer)bisalle.elementAt(i)).intValue();
//		    try {
//			    if (text.getText(bis1,1).equals("\n")) { //$NON-NLS-1$
//			        bis1++;
//			    }
//		    } catch(Exception ex) {
//		        ex.printStackTrace();
//		    }
//		    doc.setCharacterAttributes(von1, bis1-von1, doc.getStyle("translate"), false); //$NON-NLS-1$
//		}
//		
//		
//		copyright = new JTextArea(actualSong.getCopyright());
//		
//		titel.setFont((Font)parent.getOptions().get("tifo")); //$NON-NLS-1$
//		text.setFont((Font)parent.getOptions().get("tefo")); //$NON-NLS-1$
//		text.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
//		copyright.setFont((Font)parent.getOptions().get("cofo")); //$NON-NLS-1$
//		titel.setForeground((Color)parent.getOptions().get("fgco")); //$NON-NLS-1$
//		text.setForeground((Color)parent.getOptions().get("fgco")); //$NON-NLS-1$
//		copyright.setForeground((Color)parent.getOptions().get("fgco")); //$NON-NLS-1$
//		copyright.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
//		
//		titel.setRequestFocusEnabled(false);
//		text.setRequestFocusEnabled(false);
//		copyright.setRequestFocusEnabled(false);
//		
//		Point punkt = new Point(((Integer)parent.getOptions().get("sple")).intValue(), ((Integer)parent.getOptions().get("spup")).intValue()); //$NON-NLS-1$ //$NON-NLS-2$
//		
//		/*
//			Trick, damit die ÄÖÜ-Punkte in den ersten Zeilen angezeigt werden:
//			Rand oben auf 15 Pixel setzen, dann schneidet Linux die Umlaut-
//			Punkte nicht mehr ab! Natürlich muss dann die Position und Größe
//			entsprechend korrigiert werden...
//		*/
//		if (parent.getOptions().get("zt")==null ? true : (((Boolean)parent.getOptions().get("zt")).booleanValue())) { //$NON-NLS-1$ //$NON-NLS-2$
//			back.add(titel);
//			titel.setSize(new Dimension(titel.getPreferredSize().width, titel.getPreferredSize().height + 15));
//			titel.setLocation(new Point(punkt.x, punkt.y - 15));
//			titel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
//			punkt.y += titel.getHeight() + ((Integer)parent.getOptions().get("sptt")).intValue(); //$NON-NLS-1$
//		}
//		back.add(text);
//		text.setSize(new Dimension(text.getPreferredSize().width, text.getPreferredSize().height + 15));
//		text.setLocation(new Point(punkt.x, punkt.y - 15));
//		text.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
//		punkt.y += text.getHeight() + ((Integer)parent.getOptions().get("sptc")).intValue(); //$NON-NLS-1$
//		back.add(copyright);
//		copyright.setSize(new Dimension(copyright.getPreferredSize().width, copyright.getPreferredSize().height + 15));
//		copyright.setLocation(new Point(punkt.x, punkt.y - 15));
//		copyright.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
//		
//		// jetzt die Größe von "back" setzen, passend zu allen darauf liegenden Components
//		//                        Breite, Höhe
//		Dimension size = new Dimension(1, 1);
//		Component[] components = back.getComponents();
//		for ( int i = 0;
//			  i < components.length;
//			  i++ ) {
//			int component_max_x = components[i]
//								  .getX() + components[i].getWidth();
//			int component_max_y = components[i].getY() + components[i].getHeight();
//			if ( component_max_x > size.getWidth() ) {
//				size.width = component_max_x;
//			}
//			if ( component_max_y > size.getHeight() ) {
//				size.height = component_max_y;
//			}
//		}
//		size.height = size.height + ((Integer)parent.getOptions().get("spdo")).intValue(); //$NON-NLS-1$
//		back.setPreferredSize(size);
//		JTextArea oben = new JTextArea(" "); //$NON-NLS-1$
//		back.add(oben);
//		oben.setSize(oben.getPreferredSize());
//		oben.setLocation(new Point(0, 0));
//		oben.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
//		//scroll(back, TOP);
//		int[] pixels = new int[16 * 16];
//		Image image = Toolkit.getDefaultToolkit().createImage(
//						  new MemoryImageSource(16, 16, pixels, 0, 16));
//		Cursor transparentCursor =
//			Toolkit.getDefaultToolkit().createCustomCursor
//			(image, new Point(0, 0), "invisiblecursor"); //$NON-NLS-1$
//		this.setCursor(transparentCursor);
//		back.setCursor(transparentCursor);
//		scrollPane.setCursor(transparentCursor);
//		titel.setCursor(transparentCursor);
//		text.setCursor(transparentCursor);
//		copyright.setCursor(transparentCursor);
//		
//		
//		
//		
//		
//	}

	public void exit() {
		int answer = JOptionPane.YES_OPTION;
		if (!parent.closing) {
			try {
				answer = JOptionPane.showConfirmDialog(parent, Messages.getString("BeamerView.53"), Messages.getString("BeamerView.54"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (answer == JOptionPane.YES_OPTION ) {
			parent.setBeamerViewToNull();
			dispose();
		}
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public Song getActualSong() {
		return actualSong;
	}
	
	
	public static final int	NONE = 0, TOP = 1, VCENTER = 2, BOTTOM = 4, LEFT = 8, HCENTER = 16, RIGHT = 32;
	
	public static void scroll(JComponent c, int part) {
		scroll(c, part & (LEFT | HCENTER | RIGHT), part & (TOP | VCENTER | BOTTOM));
	}
	
	public static void scroll(JComponent c, int horizontal, int vertical) {
		Rectangle visible = c.getVisibleRect();
		if (visible.height == 0 ) {
			visible.height = 1;
		}
		if (visible.width == 0) {
			visible.width = 1;
		}
		
		Rectangle bounds = c.getBounds();
		
		switch (vertical) {
			case TOP:
				visible.y = 0;
				break;
			case VCENTER:
				visible.y = (bounds.height - visible.height) / 2;
				break;
			case BOTTOM:
				visible.y = bounds.height - visible.height;
				break;
		}
		
		switch (horizontal) {
			case LEFT:
				visible.x = 0;
				break;
			case HCENTER:
				visible.x = (bounds.width - visible.width) / 2;
				break;
			case RIGHT:
				visible.x = bounds.width - visible.width;
				break;
		}
		
		c.scrollRectToVisible(visible);
	}
	
	private void blackScreen(boolean yes) {
		if (yes) {
			JPanel panel = new JPanel();
			panel.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
			this.setContentPane(panel);
		} else {
			back.validate();
			this.setContentPane(scrollPane.getViewport());
		}
	}
	
}
