package org.zephyrsoft.sdb;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.NavigationFilter.*;
import javax.swing.text.Position.*;

import org.jdesktop.animation.transitions.*;
import org.zephyrsoft.sdb.structure.*;
import org.zephyrsoft.util.*;

/**
 * Beamer-Projektionsansicht für die Lieder-Datenbank
 * @author Mathis Dirksen-Thedens
 */

public class BeamerView extends JFrame implements TransitionTarget {

	public static int FOIL_NUMBER_TO_SHOW_LOGO = -123;
	
	private boolean debug = false;
	
	private boolean specialMode = true;
	
	JPanel contentPane = null;
	BorderLayout borderLayout = null;
	JScrollPane scrollPane = null;
	JPanel back = null;
	
	private JLabel titel;
	private MyJTextPane text;
	private List textPositions;
	private List textParts;
	
	private int moveToPosition = 0;
	
	private Song actualSong = null;
	
	private int actualFoil = 0;
	private Font titelfont = null;
	private Font textfont = null;
	private Font translatefont = null;
	private Font copyrightfont = null;
	private ImagePanel logo = null;
	private JLabel logolabel = null;
	
	private BeamerGUI parent;
	
	private Dimension dim = null;
	
	public BeamerView(Song song, Font titelfont, Font textfont, Font translatefont, Font copyrightfont, ImagePanel logo, boolean printAccords, BeamerGUI parent, GraphicsConfiguration gc, int foil) {
		//super(parent, gc);
		super(Messages.getString("BeamerView.0"), gc); //$NON-NLS-1$
		setIconImage(LookAndFeelUtil.getIcon());
		this.titelfont = titelfont;
		this.textfont = textfont;
		this.translatefont = translatefont;
		this.copyrightfont = copyrightfont;
		this.logo = logo;
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
		if (scrollPane!=null) {
			scrollPane.setCursor(transparentCursor);
		}
		if (titel!=null) {
			titel.setCursor(transparentCursor);
		}
		if (text!=null) {
			text.setCursor(transparentCursor);
		}
//		copyright.setCursor(transparentCursor);
		setResizable(false);
		setUndecorated(true);
		this.getRootPane().setWindowDecorationStyle(NONE);
		pack();
		show();
		
		RepaintManager.currentManager(back).setDoubleBufferingEnabled(true);
		if (specialMode) {
			// Performance-Tuning:
			scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
		}
		
		// groß auf letztem verfügbaren Bildschirm machen (wird wohl der Beamer sein):
		try {
			this.setSize(dim);
			if (scrollPane!=null) {
				scrollPane.getViewport().setSize(dim);
			}
			
			// Fullscreen!
			//System.out.println("FULLSCREENSUPPORTED=" + device.isFullScreenSupported());
			//device.setFullScreenWindow(this);
			
			this.validate();
		} catch (Exception ex) {
			//device.setFullScreenWindow(null);
			System.out.println("ERROR WHILE SETTING WINDOW TO FULLSCREEN: " + ex.getMessage()); //$NON-NLS-1$
		}
	}
	
	public void scroll_down() {
		int scrollingIsAt = scrollPane.getVerticalScrollBar().getValue();
		Rectangle rect = back.getVisibleRect();
		rect.setLocation(rect.x, rect.y + ((Integer)parent.parent.getOptions().get("scroll")).intValue()); //$NON-NLS-1$
		if (rect.y > back.getHeight()-rect.height) {
			rect.setLocation(rect.x, back.getHeight()-rect.height);
		}
		back.scrollRectToVisible(rect);
	}
	
	public void scroll_up() {
		int scrollingIsAt = scrollPane.getVerticalScrollBar().getValue();
		Rectangle rect = back.getVisibleRect();
		rect.setLocation(rect.x, rect.y - ((Integer)parent.parent.getOptions().get("scroll")).intValue()); //$NON-NLS-1$
		if (rect.y > back.getHeight()-rect.height) {
			rect.setLocation(rect.x, back.getHeight()-rect.height);
		}
		back.scrollRectToVisible(rect);
	}
	
	public Song getSong() {
		return actualSong;
	}
	
	public void showSong(Song newsong, Font titelfont, Font textfont, Font translatefont, Font copyrightfont, boolean printAccords, int foil) {
	    this.titelfont = titelfont;
		this.textfont = textfont;
		this.translatefont = translatefont;
		this.copyrightfont = copyrightfont;
		actualSong = newsong;
		actualFoil = foil;
		
		if (back!=null) {
			back.removeAll();
		}
		
		back = new JPanel();
		back.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
		back.setLayout(new BorderLayout());
		
		if (foil==BeamerView.FOIL_NUMBER_TO_SHOW_LOGO) {
			// nur Logo-Anzeige gewünscht
			if (logo!=null) {
				back.add(logo);
			}
			scrollPane = new JScrollPane(back);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setDoubleBuffered(true);
			
			this.setContentPane(scrollPane.getViewport());
			
		} else {
			// GUI formen
			scrollPane = new JScrollPane(back);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setDoubleBuffered(true);
			
			this.setContentPane(scrollPane.getViewport());
			
			if (specialMode) {
				// nur so ohne Flackern am Anfang:
				scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
			}
			
			try {
				this.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
				this.getRootPane().setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
				this.getContentPane().setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			back.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
			back.repaint();
			
			titel = new JLabel((newsong!=null ? parent.getNormalTitleByID(newsong.getID()) : "")); //$NON-NLS-1$
			text = new MyJTextPane();
			
			if (((Boolean)parent.parent.getOptions().get("mf")).booleanValue()) { //$NON-NLS-1$
				if ( printAccords ) {
					text.setText((newsong!=null ? newsong.getTextAndAccordsInFont_Foil(textfont, false, foil) : "")); //$NON-NLS-1$
				} else {
					text.setText((newsong!=null ? newsong.getOnlyText_Foil(foil) : "")); //$NON-NLS-1$
				}
			} else {
				if ( printAccords ) {
					text.setText((newsong!=null ? newsong.getTextAndAccordsInFont(textfont, false) : "")); //$NON-NLS-1$
				} else {
					text.setText((newsong!=null ? newsong.getOnlyText() : "")); //$NON-NLS-1$
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
			text.setText(text1 + (newsong!=null ? "\n" + newsong.getCopyright() : "")); //$NON-NLS-1$ //$NON-NLS-2$
			
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
			StyleConstants.setFontSize(sptc, ((Integer)parent.getOptions().get("sptc")).intValue()); //$NON-NLS-1$
			
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
			doc.setCharacterAttributes(text1.length() + 1, text1.length() + (newsong!=null ? 1 + newsong.getCopyright().length() : 0), doc.getStyle("copyright"), false); //$NON-NLS-1$
	
			
			titel.setFont((Font)parent.getOptions().get("tifo")); //$NON-NLS-1$
			text.setFont((Font)parent.getOptions().get("tefo")); //$NON-NLS-1$
			text.setBackground((Color)parent.getOptions().get("bgco")); //$NON-NLS-1$
			titel.setForeground((Color)parent.getOptions().get("fgco")); //$NON-NLS-1$
			text.setForeground((Color)parent.getOptions().get("fgco")); //$NON-NLS-1$
			
			titel.setRequestFocusEnabled(false);
			text.setRequestFocusEnabled(false);
			
			/*
				Trick, damit die äöü-Punkte in den ersten Zeilen angezeigt werden:
				Rand oben auf 15 Pixel setzen, dann schneidet Linux die Umlaut-
				Punkte nicht mehr ab! Natürlich muss dann die Position und Größe
				entsprechend korrigiert werden...
			*/
			text.setBorder(BorderFactory.createEmptyBorder(((Integer)parent.getOptions().get(((parent.getOptions().get("zt")==null ? true : (((Boolean)parent.getOptions().get("zt")).booleanValue())) ? "sptt" : "spup"))).intValue(), ((Integer)parent.getOptions().get("sple")).intValue(),0,15)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			text.setEditable(false);
			back.add(text, BorderLayout.CENTER);
			back.setBorder(BorderFactory.createEmptyBorder(0, 0, ((Integer)parent.getOptions().get("spdo")).intValue(), 0)); //$NON-NLS-1$
			if (parent.getOptions().get("zt")==null ? true : (((Boolean)parent.getOptions().get("zt")).booleanValue())) { //$NON-NLS-1$ //$NON-NLS-2$
			    titel.setBorder(BorderFactory.createEmptyBorder(((Integer)parent.getOptions().get("spup")).intValue(), ((Integer)parent.getOptions().get("sple")).intValue(),15,15)); //$NON-NLS-1$ //$NON-NLS-2$
				back.add(titel, BorderLayout.NORTH);
			}
			
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
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					calculateTextPositions();
					parent.updateJumpButtons();
					parent.initAnimator();
					parent.initTransitionRun();
					parent.validate();
				}
			});
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BeamerView.this.validate();
				if (specialMode) {
					// für schnelles Scrollen ohne Ruckeln:
					scrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
				}
			}
		});
	}
	
	public void calculateTextPositions() {
		// Positionen der Absätze speichern
		textParts = new ArrayList();
		textPositions = new ArrayList();
		textPositions.add(Integer.valueOf(0));
		String[] parts = text.getText().split("\n\n"); //$NON-NLS-1$
		int cursorPos = 0;
		for (int i = 0; i < parts.length; i++) {
			textParts.add(parts[i]);
			// +2 because the delimiter has to be counted too:
			cursorPos += parts[i].length() + 2;
			if (cursorPos <= text.getText().length()) {
				Rectangle r = null;
				try {
					r = text.modelToView(cursorPos);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				if (r != null) {
					textPositions.add(Integer.valueOf(Double.valueOf(r.getY()).intValue()) - ((Integer)parent.getOptions().get("spup")).intValue());
				}
			}
		}
	}
	
	public List[] getTextWithPositions() {
		return new List[] {textParts, textPositions};
	}
	
	public int nextFoil() {
		return nextFoil(false);
	}
	public int nextFoil(boolean printAccords) {
		if (actualSong==null) {
			return actualFoil;
		}
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

	public void setupNextScreen() {
		scrollPane.getViewport().setViewPosition(new Point(0, getMoveToPosition()));
	}

	public int getMoveToPosition() {
		return moveToPosition;
	}

	public void setMoveToPosition(int moveToPosition) {
		this.moveToPosition = moveToPosition;
	}
	
	/**
	 * customized JTextPane for no-flicker text display
	 */
	protected class MyJTextPane extends JTextPane {
		private static final long serialVersionUID = 1L;
		
		public MyJTextPane() {
			super();
			caretNoUpdate();
		}
		
        public MyJTextPane(StyledDocument doc) {
        	super(doc);
        	caretNoUpdate();
        }
        
        private void caretNoUpdate() {
        	DefaultCaret caret = (DefaultCaret) getCaret();
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        }

		public Dimension getPreferredSize(){
	        Dimension size = super.getPreferredSize();
	        // "sple"="space on left side"; 15="space on right side"
	        size.width = getSize().width - ((Integer)parent.getOptions().get("sple")).intValue() - 15; //$NON-NLS-1$
	        return size;
	    }
	}
	
}
